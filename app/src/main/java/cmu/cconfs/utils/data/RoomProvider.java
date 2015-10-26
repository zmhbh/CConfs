package cmu.cconfs.utils.data;

import android.support.v4.util.Pair;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cmu.cconfs.model.parseModel.Session_Room;
import cmu.cconfs.model.parseModel.Room;

/**
 * Created by Wendy_Guo on 10/6/15.
 */
public class RoomProvider {
    private final int count = 6;

    private List<RoomDataProvider> programsData;

    public RoomDataProvider getRoomDataProvider(int roomIndex) {
        return programsData.get(roomIndex);
    }

    public RoomProvider() {
        programsData = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            RoomDataProvider list = new RoomDataProvider();
            programsData.add(list);
        }
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < programsData.size(); i++) {
            RoomDataProvider list = programsData.get(i);
            ParseQuery query = Room.getQuery();
            query.fromLocalDatastore();
            query.fromPin(Room.PIN_TAG);
            query.whereEqualTo("program_id", i + 1);
            query.orderByAscending("room_id");

            // groupItems
            List<Room> results = null;
            try {
                results = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("XXXX!!!!@@@@@@@: ", "results.size: " + results.size());

            for (int j = 0; j < results.size(); j++) {
                final long groupId = j;
                final RoomDataProvider.ConcreteGroupData group = new RoomDataProvider.ConcreteGroupData(groupId, results.get(j).getValue());
                final List<RoomDataProvider.ConcreteChildData> children = new ArrayList<>();
                query = Session_Room.getQuery();
                query.fromLocalDatastore();
                query.fromPin(Session_Room.PIN_TAG);
                query.whereEqualTo("room_id", results.get(j).getRoomId());
                query.orderByAscending("session_id");
                //childItems
                List<Session_Room> sessionResults = null;

                try {
                    sessionResults = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                HashMap<String,HashSet<String>> record = new HashMap<String,HashSet<String>>();
                for (int k = 0; k < sessionResults.size(); k++) {
                    final long childId = group.generateNewChildId();
                    Session_Room sessionResult = sessionResults.get(k);
                    if(sessionResult.getSessionTitle() != null && sessionResult.getSessionTitle().length()>0) {
                        if (record.containsKey(sessionResult.getTimeslot())){
                            if (record.get(sessionResult.getTimeslot()).contains(sessionResult.getSessionTitle()))
                                continue;
                            else {
                                record.get(sessionResult.getTimeslot()).add(sessionResult.getSessionTitle());
                                children.add(new RoomDataProvider.ConcreteChildData(childId, sessionResult));
                            }
                        }
                        else {
                            HashSet<String> set = new HashSet<String>();
                            set.add(sessionResult.getSessionTitle());
                            record.put(sessionResult.getTimeslot(),set);
                            children.add(new RoomDataProvider.ConcreteChildData(childId, sessionResult));
                        }
                    }
                }
                if(children.size() != 0)

                    list.add(new Pair<RoomDataProvider.ConcreteGroupData, List<RoomDataProvider.ConcreteChildData>>(group, children));

            }


        }
    }

}
