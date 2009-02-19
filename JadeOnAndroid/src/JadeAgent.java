import jade.wrapper.gateway.GatewayAgent;


public class JadeAgent extends GatewayAgent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
	  	System.out.println("Hello World! My name is "+getLocalName());
	  	
	  	// Make this agent terminate
	  	doDelete();
	  } 

}
