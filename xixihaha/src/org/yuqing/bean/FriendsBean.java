package org.yuqing.bean;

/**
 *  用户关系：朋友列表
 *  @author 北邮君君  weibobee@gmail.com   2011-9-10
 *
 */

public class FriendsBean
{

	private String id;          //用户id
    private String friendsIds;  //朋友的id
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFriendsIds() {
		return friendsIds;
	}
	public void setFriendsIds(String friendsIds) {
		this.friendsIds = friendsIds;
	}
	
    @Override
    public String toString() 
    {
    	  return id +", " + friendsIds;
    }
}
