package com.example.decu.allfloormeetingroomstatus;

public class Model {

    int roomId;
    int floorId;
    String roomName;
    int physicalStatus;
    int outlookStatus;
    long timeStamp;

    public String getRoomName() {
        return roomName;
    }


    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId)
    {
        this.roomId=roomId;
    }

    public void setFloorId(int floorId)
    {
        this.floorId=floorId;
    }

    public int getFloorId() {
        return floorId;
    }


    public int getPhysicalStatus() {
        return physicalStatus;
    }
    public void setPhysicalStatus(int status) {
        this.physicalStatus = status;
    }


    public int getOutlookStatus() {
        return outlookStatus;
    }
    public void setOutlookStatus(int status) {
        this.outlookStatus = status;
    }







    public long getTimeStamp() {
        return timeStamp;
    }


    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    

}
