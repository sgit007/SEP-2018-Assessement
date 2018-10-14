package uts.sep.westfieldparkmate.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ParkingLot {

    private String pID;
    private boolean status;

    public ParkingLot() {
        // Default constructor required for calls to DataSnapshot.getValue(Parking.class)
    }

    public ParkingLot(String pID, boolean status) {
        this.pID = pID;
        this.status = status;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
