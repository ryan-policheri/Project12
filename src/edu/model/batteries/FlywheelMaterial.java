package edu.model.batteries;

public class FlywheelMaterial
{
	private final String materialName;
	private final double densityOfMaterialInKilogramMetersCubed;
	private final double tensileStressOfMaterialInPascals;
	
	public FlywheelMaterial(String materialName, double densityOfMaterialInKilogramMetersCubed, double tensileStressOfMaterialInPascals)
	{
		this.materialName = materialName;
		this.densityOfMaterialInKilogramMetersCubed = densityOfMaterialInKilogramMetersCubed;
		this.tensileStressOfMaterialInPascals = tensileStressOfMaterialInPascals;
	}

	public FlywheelMaterial(String materialName)
	{
		this.materialName = materialName;

		// give the material values based on the name of the material
		switch (materialName)
		{
			case "Titanium":
				this.densityOfMaterialInKilogramMetersCubed = 4506;
				this.tensileStressOfMaterialInPascals = 8.8 * java.lang.Math.pow(10, 8);
				break;
			case "Carbon Fiber":
				this.densityOfMaterialInKilogramMetersCubed = 1799;
				this.tensileStressOfMaterialInPascals = 4000000000.0;
				break;
			case "Steel":
				this.densityOfMaterialInKilogramMetersCubed = 8050;
				this.tensileStressOfMaterialInPascals = 6.9 * java.lang.Math.pow(10, 8);
				break;
			case "Aluminum":
				this.densityOfMaterialInKilogramMetersCubed = 2700;
				this.tensileStressOfMaterialInPascals = 5 * java.lang.Math.pow(10, 8);
				break;
			default:
				this.densityOfMaterialInKilogramMetersCubed = 0;
				this.tensileStressOfMaterialInPascals = 0;
				break;
		}
	}
	
	public double calculateMaxAngularVelocity(double radiusInMeters)
	{
		//Equation at http://large.stanford.edu/courses/2010/ph240/wheeler1/
		
		double radiusSquared = radiusInMeters * radiusInMeters;
		double radiusSquaredMultipliedByDensity = radiusSquared * this.densityOfMaterialInKilogramMetersCubed;
		double tensileStressDividedRadiusSquaredMultipliedByDensity = this.tensileStressOfMaterialInPascals / radiusSquaredMultipliedByDensity;
		double maxAngularVelocity = Math.sqrt(tensileStressDividedRadiusSquaredMultipliedByDensity);
		
		return maxAngularVelocity;
	}
	
	public void displayMaterial()
	{
		String displayString = "The flywheel is made of " + this.materialName;
		System.out.println(displayString);
	}

	@Override
	public String toString()
	{
		return "FlywheelMaterial: " +
				"materialName='" + this.materialName + '\'' +
				", densityOfMaterialInKilogramMetersCubed=" + this.densityOfMaterialInKilogramMetersCubed +
				", tensileStressOfMaterialInPascals=" + this.tensileStressOfMaterialInPascals;
	}
}
