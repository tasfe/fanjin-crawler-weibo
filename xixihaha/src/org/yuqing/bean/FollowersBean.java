package org.yuqing.bean;

/**
 *  用户关系：粉丝列表
 *  @author 北邮君君  weibobee@gmail.com   2011-9-10
 *
 */

public class FollowersBean
{

	private String id;          //用户id
    private String followersIds;  //粉丝的id
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFriendsIds() {
		return followersIds;
	}
	public void setFriendsIds(String friendsIds) {
		this.followersIds = friendsIds;
	}
	
    @Override
    public String toString() 
    {
    	  return id +", " + followersIds;
    }
}
