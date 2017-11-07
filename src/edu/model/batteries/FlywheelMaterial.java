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
	
	
}
