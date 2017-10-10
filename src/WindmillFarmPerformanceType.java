
public class WindmillFarmPerformanceType
{
	private String performanceType = "Moderate";       
	
	public WindmillFarmPerformanceType()
	{
	}
	
	public void setToHighPerformance()
	{
		this.performanceType = "High";
	}
	
	public void setToModeratePerformance()
	{
		this.performanceType = "Moderate";
	}
	
	public void setToLowPerformance()
	{
		this.performanceType = "Low";
	}
	
	public String returnPerformanceType()
	{
		return this.performanceType;
	}
	
	public String toString()
	{
		return this.performanceType;
	}

}
