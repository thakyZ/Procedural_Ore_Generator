package config;

public abstract class CommonConfig {
	
	public float patchSizeMultiplier = -1;
	public float patchSizeVariance = -1;
	public int maxOreTiles = -1;
	public int maxOrePatches = -1;
	public Long seed = null;
	public double p = -1;
	public float density = -1;
	public int shape = -1;
	public Boolean avoidIce = null;
	public int surfaceArea = -1;
	public Boolean surfaceHintMaps = null;
	public float surfaceHintProbability = -1;
	public int surfaceHintColour = -1;
	public Boolean makeColouredMaps = null;
	public String testColourHex = null;
	public int startDepth = -1;
	public int depth = -1;
	public String mappingFileTargetColour = null;
	public int mappingFileColourInfluence = -1;
	public int centreOreTile = -1;
	
	public void cascadeSettings(CommonConfig other) {
		if(patchSizeMultiplier == -1) {
			patchSizeMultiplier = other.patchSizeMultiplier;
		}
		if(patchSizeVariance == -1) {
			patchSizeVariance = other.patchSizeVariance;
		}
		if(maxOreTiles == -1) {
			maxOreTiles = other.maxOreTiles;
		}
		if(maxOrePatches == -1) {
			maxOrePatches = other.maxOrePatches;
		}
		if(seed == null) {
			seed = other.seed;
		}
		if(p == -1) {
			p = other.p;
		}
		if(density == -1) {
			density = other.density;
		}
		if(shape == -1) {
			shape = other.shape;
		}
		if(avoidIce == null) {
			avoidIce = other.avoidIce;
		}
		if(surfaceArea == -1) {
			surfaceArea = other.surfaceArea;
		}
		if(surfaceHintMaps == null) {
			surfaceHintMaps = other.surfaceHintMaps;
		}
		if(surfaceHintProbability == -1) {
			surfaceHintProbability = other.surfaceHintProbability;
		}
		if(surfaceHintColour == -1) {
			surfaceHintColour = other.surfaceHintColour;
		}
		if(makeColouredMaps == null) {
			makeColouredMaps = other.makeColouredMaps;
		}
		if(testColourHex == null) {
			testColourHex = other.testColourHex;
		}
		if(startDepth == -1) {
			startDepth = other.startDepth;
		}
		if(depth == -1) {
			depth = other.depth;
		}
		if(mappingFileTargetColour == null) {
			mappingFileTargetColour = other.mappingFileTargetColour;
		}
		if(mappingFileColourInfluence == -1) {
			mappingFileColourInfluence = other.mappingFileColourInfluence;
		}
		if(centreOreTile == -1) {
			centreOreTile = other.centreOreTile;
		}
	}
	
	public void setDefaults() {
		patchSizeMultiplier = 1.0f;
		patchSizeVariance = 0.4f;
		maxOreTiles = 100000;
		maxOrePatches = 1000;
		seed = 7l;
		p = 0.0;
		density = 1.0f;
		shape = 1;
		avoidIce = true;
		surfaceArea = 20;
		surfaceHintMaps = true;
		surfaceHintProbability = 1.0f;
		surfaceHintColour = 128;
		makeColouredMaps = true;
		testColourHex = "FFFFFF";
		startDepth = 10;
		depth = 6;
		mappingFileTargetColour = "#616c83";
		mappingFileColourInfluence = 15;
		centreOreTile = -1;
	}
}
