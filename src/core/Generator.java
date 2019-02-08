package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import config.OreConfig;
import config.PlanetConfig;
import map.MapData;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import static map.MapData.OPAQUE;
import static map.MapData.ICE_FILTER;
import static map.MapData.ORE_EXCLUDER;
import static map.MapData.ORE_FILTER;

public class Generator {
	
	public long generatePatches(MapData mapData, PlanetConfig planetConfig) {
		List<Pair<OreConfig, Double>> tempPairedList = new ArrayList<Pair<OreConfig, Double>>();
		Map<OreConfig, Long> oreTileCounts = new HashMap<OreConfig, Long>();
		for(OreConfig ore : planetConfig.ores) {
			tempPairedList.add(new Pair<OreConfig, Double>(ore, ore.p));
			oreTileCounts.put(ore, new Long(0));
		}
		EnumeratedDistribution<OreConfig> oreDist = new EnumeratedDistribution<OreConfig>(tempPairedList);
		Random rand = new Random(planetConfig.seed);
		oreDist.reseedRandomGenerator(rand.nextLong());
		long generatedTiles = 0;
		for(int i = 0; i < planetConfig.maxOrePatches && generatedTiles < planetConfig.maxOreTiles; ++i) {
			OreConfig ore = oreDist.sample();
			int generatedTilesForOre = generateOrePatch(mapData, ore, rand);
			generatedTiles += generatedTilesForOre;
			oreTileCounts.put(ore, oreTileCounts.get(ore).longValue() + generatedTilesForOre);
		}

		for(OreConfig ore : oreTileCounts.keySet()) {
			System.out.println("\t\tTiles generated for ore " + ore.type + " (id:" + ore.id + ") with testColour " + Integer.toHexString(ore.testColour) + " : " + oreTileCounts.get(ore));
		}
		return generatedTiles;
	}
	
	private int generateOrePatch(MapData mapData, OreConfig ore, Random rand) {
		int tilesAdded = 0;
		int mapSize = mapData.getMapSize();
		int mapIndex = rand.nextInt(MapData.MAP_SIDES);
		int startColIndex = rand.nextInt(mapSize);
		int startRowIndex = rand.nextInt(mapSize);
		Random tileRand = new Random(rand.nextLong());
		Random hintRand = new Random(rand.nextLong());
		int patchSize = Math.round((ore.surfaceArea * ore.patchSizeMultiplier) * (1 + (tileRand.nextFloat() * 2 - 1) * ore.patchSizeVariance));
		float patchRadius = Math.round(Math.sqrt((double)patchSize) / (ore.density * 3));
		float squash = tileRand.nextFloat() * 1.0f + 0.75f;
		float horizontalSquash;
		float verticalSquash;
		if(tileRand.nextBoolean()) {
			horizontalSquash = squash;
			verticalSquash = 1 / squash;
		}
		else {
			verticalSquash = squash;
			horizontalSquash = 1 / squash;			
		}
		
		int oreId = ore.centreOreTile == -1 ? ore.id : ore.centreOreTile;
		
		boolean avoidIce = ore.avoidIce;
		boolean isSurfaceHint = ore.surfaceHintMaps && ore.surfaceHintProbability > 0;
		int colIndex = startColIndex;
		int rowIndex = startRowIndex;
		int centreOreTile = ore.centreOreTile;
		BufferedImage img = mapData.images[mapIndex];
		BufferedImage hintImg = mapData.surfaceHintImages[mapIndex];
		BufferedImage colouredImg = mapData.colouredMaps[mapIndex];
		RandomGenerator randGen = new JDKRandomGenerator();
		randGen.setSeed(tileRand.nextInt());
		int iterations = 0;
		int maxIterations = patchSize * 20;
		
		do {
			//add patch tiles
			//each tile/pixel corresponds to a 30x30m patch of ore in game - measured on EarthLike
			if(rowIndex < mapSize && rowIndex >= 0 && colIndex < mapSize && colIndex >= 0) {
				int pixRGB = img.getRGB(colIndex, rowIndex);
				if((pixRGB | ORE_FILTER) == oreId || (centreOreTile != 0 && (pixRGB | ORE_FILTER) == centreOreTile)) { 
					continue;
				}
				if((avoidIce && ((pixRGB & ICE_FILTER) == ICE_FILTER))) {
					if(iterations == 0) {
						return 0;
					}
					else {
						continue;
					}
				}
				img.setRGB(colIndex, rowIndex, (img.getRGB(colIndex, rowIndex) & ORE_EXCLUDER) | oreId);
				++tilesAdded;
				if(ore.makeColouredMaps) {
					colouredImg.setRGB(colIndex, rowIndex, ore.testColour);
				}
				if(isSurfaceHint && hintRand.nextFloat() < ore.surfaceHintProbability) {
					hintImg.setRGB(colIndex, rowIndex, ore.surfaceHintColour);
				}
			}
			if(centreOreTile != -1 && iterations == 0) {
				oreId = ore.id;
			}
			colIndex = startColIndex + Math.round((float)(randGen.nextGaussian() * patchRadius) * horizontalSquash);
			rowIndex = startRowIndex + Math.round((float)(randGen.nextGaussian() * patchRadius) * verticalSquash);
		} while(tilesAdded < patchSize && ++iterations < maxIterations);
		return tilesAdded;
	}
	
}
