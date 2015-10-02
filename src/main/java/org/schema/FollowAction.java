package org.schema;

/**
 * 
 * The act of forming a personal connection with someone/something (object)
 * unidirectionally/asymmetrically to get updates polled from.
 * <p>
 * Related actions:
 * </p>
 * <ul>
 * <li><a href="http://schema.org/BefriendAction">BefriendAction</a>: Unlike
 * BefriendAction, FollowAction implies that the connection is *not* necessarily
 * reciprocal.</li>
 * <li><a href="http://schema.org/SubscribeAction">SubscribeAction</a>: Unlike
 * SubscribeAction, FollowAction implies that the follower acts as an active
 * agent constantly/actively polling for updates.</li>
 * <li><a href="http://schema.org/RegisterAction">RegisterAction</a>: Unlike
 * RegisterAction, FollowAction implies that the agent is interested in
 * continuing receiving updates from the object.</li>
 * <li><a href="http://schema.org/JoinAction">JoinAction</a>: Unlike JoinAction,
 * FollowAction implies that the agent is interested in getting updates from the
 * object.</li>
 * <li><a href="http://schema.org/TrackAction">TrackAction</a>: Unlike
 * TrackAction, FollowAction refers to the polling of updates of all aspects of
 * animate objects rather than the location of inanimate objects (e.g. you track
 * a package, but you don't follow it).</li>
 * </ul>
 * 
 * @fullPath Thing > Action > InteractAction > FollowAction
 * 
 * @author Texelz (by Onhate)
 * 
 */
public class FollowAction extends InteractAction {

	private Object followee;

	/**
	 * A sub property of object. The person or organization being followed.
	 * 
	 * @see Organization
	 * @see Person
	 */
	public Object getFollowee() {
		return this.followee;
	}

	/**
	 * A sub property of object. The person or organization being followed.
	 * 
	 * @see Organization
	 * @see Person
	 */
	public void setFollowee(Object followee) {
		this.followee = followee;
	}
}
