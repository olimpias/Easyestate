package com.EasyEstate.SupportTool;

import com.EasyEstate.Model.Listing;

/**
 * Created by canturker on 30/04/15.
 */
public interface Communicator {
    public void Respond(Listing listing,int favoriteFlag);
}
