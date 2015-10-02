package org.schema;

/**
 * 
 * The act of an agent communicating (service provider, social media, etc) their
 * departure of a previously reserved service (e.g. flight check in) or place
 * (e.g. hotel).
 * <p>
 * Related actions:
 * </p>
 * <ul>
 * <li><a href="http://schema.org/CheckInAction">CheckInAction</a>: The
 * antagonym of CheckOutAction.</li>
 * <li><a href="http://schema.org/DepartAction">DepartAction</a>: Unlike
 * DepartAction, CheckOutAction implies that the agent is informing/confirming
 * the end of a previously reserved service.</li>
 * <li><a href="http://schema.org/CancelAction">CancelAction</a>: Unlike
 * CancelAction, CheckOutAction implies that the agent is informing/confirming
 * the end of a previously reserved service.</li>
 * </ul>
 * 
 * @fullPath Thing > Action > InteractAction > CommunicateAction >
 *           CheckOutAction
 * 
 * @author Texelz (by Onhate)
 * 
 */
public class CheckOutAction extends CommunicateAction {

}
