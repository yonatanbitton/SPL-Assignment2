package bgu.spl.a2.sim.conf;
import com.google.gson.annotations.*;



/**
 * This class represent the Source - A class for the input from the json file. 
 * This class is used only for the convertion from json.
 */
public class Source {
	@SerializedName("threads")
	int threads;

	@SerializedName("tools")
	public ToolClass[] tools;
	 
	@SerializedName("plans")
	public ManufactoringPlan[] plans;
	
	@SerializedName("waves")
	public Request[][] waves;
	
	
	/** Constructor
	 *@param threads - the number of threads
	 *@param threads - the number of threads
	 *@param threads - the number of threads
	 *@param threads - the number of threads
	 */
	public Source (int threads, ToolClass[] tools, ManufactoringPlan[] plans, Request[][] waves){
		this.threads=threads;
		this.tools=tools;
		this.plans=plans;
		this.waves=waves;
	}
	

	/** 
	 *@return the number of threads
	 */
	public int getThreads(){
		return threads;
	}
	
	/** 
	 *@return tools array 
	 */
	public ToolClass[] getTools(){
		return tools;
	}

	/** 
	 *@return waves array
	 */
	public Request[][] getRequests(){
		return waves;
	}

	/** 
	 *@return plans array
	 */
	public ManufactoringPlan[] getPlans(){
		return plans;
	}

	}


	
