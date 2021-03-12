package io.blackpine.sustain.queryServiceClient;

import org.sustain.CompoundRequest;
import org.sustain.CompoundResponse;
import org.sustain.DirectRequest;
import org.sustain.DirectResponse;
import org.sustain.Query;
import org.sustain.SustainGrpc;

import java.util.Iterator;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    protected static final int PORT = 50051;

    protected static final String MACAV2_QUERY = "[{\"$match\":{\"gis_join\":{\"$in\":[\"G0800350\",\"G0800130\",\"G0800570\",\"G0800470\",\"G0800690\",\"G0800590\",\"G0800370\",\"G0800050\",\"G0800490\",\"G0800390\",\"G0800190\",\"G0801210\",\"G3100330\",\"G5600070\",\"G5600010\",\"G5600210\",\"G0800140\",\"G0801230\",\"G0801170\",\"G0801070\",\"G3101050\",\"G0800930\",\"G0800730\",\"G0800630\",\"G0800750\",\"G0800870\",\"G0800310\",\"G0800970\",\"G0800650\",\"G0800010\",\"G0800150\",\"G0800290\",\"G0801030\",\"G0801190\",\"G0800510\",\"G0800410\",\"G0800450\",\"G0800770\",\"G0800810\",\"G5600150\",\"G5600370\",\"G3100070\",\"G3101230\"]}}}]";
    //protected static final String MACAV2_QUERY = "[{\"$match\":{\"gis_join\":{\"$in\":[\"G0800750\",\"G0800870\",\"G0800310\",\"G0800970\",\"G0800650\",\"G0800010\",\"G0800150\",\"G0800290\",\"G0801030\",\"G0801190\",\"G0800510\",\"G0800410\",\"G0800450\",\"G0800770\",\"G0800810\",\"G5600150\",\"G5600370\",\"G3100070\",\"G3101230\"]}}}]";

    protected static final String WIDE_ZOOM_QUERY = "[{\"$match\":{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-107.30621337890626,39.44679856427205],[-107.30621337890626,41.46537017728069],[-103.15338134765625,41.46537017728069],[-103.15338134765625,39.44679856427205],[-107.30621337890626,39.44679856427205]]]}}}}}]";
    //protected static final String WIDE_ZOOM_QUERY = "{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-107.30621337890626,39.44679856427205],[-107.30621337890626,41.46537017728069],[-103.15338134765625,41.46537017728069],[-103.15338134765625,39.44679856427205],[-107.30621337890626,39.44679856427205]]]}}}}";

    protected static final String COLORADO_QUERY = "[{\"$match\":{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-109.055135,37.003002],[-109.055135,41.004744],[-102.053403,41.004744],[-102.053403,37.003002],[-109.055135,37.003002]]]}}}}}]";
    //protected static final String COLORADO_QUERY = "{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-109.055135,37.003002],[-109.055135,41.004744],[-102.053403,41.004744],[-102.053403,37.003002],[-109.055135,37.003002]]]}}}}";

    protected static final String QUARTER_UNITED_STATES_QUERY = "[{\"$match\":{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,30.719813],[-66.719844,30.719813],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}}]";
    //protected static final String QUARTER_UNITED_STATES_QUERY = "{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,30.719813],[-66.719844,30.719813],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}";

    protected static final String HALF_UNITED_STATES_QUERY = "[{\"$match\":{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,36.900688],[-66.719844,36.900688],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}}]";
    //protected static final String HALF_UNITED_STATES_QUERY = "{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,36.900688],[-66.719844,36.900688],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}";

    protected static final String UNITED_STATES_QUERY = "[{\"$match\":{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,49.262438],[-66.719844,49.262438],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}}]";
    //protected static final String UNITED_STATES_QUERY = "{\"geometry\":{\"$geoIntersects\":{\"$geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-125.480120,24.538938],[-125.480120,49.262438],[-66.719844,49.262438],[-66.719844,24.538938],[-125.480120,24.538938]]]}}}}";

    public static void main(String[] args) {
        // validate arguments
        if (args.length != 1) {
            System.out.println("Usage: ./APP <iterations>");
            System.exit(1);
        }

        int iterations = Integer.parseInt(args[0]);
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();

            // open grpc channel
            ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", PORT)
                .usePlaintext()
                .build();

            // initialize blocking stub
            SustainGrpc.SustainBlockingStub blockingStub =
                SustainGrpc.newBlockingStub(channel);

            DirectRequest request = DirectRequest.newBuilder()
                //.setCollection("region_county")
                //.setQuery(UNITED_STATES_QUERY)
                .setCollection("macav2_county")
                .setQuery(MACAV2_QUERY)
                .build();

            Iterator<DirectResponse> iterator =
                blockingStub.directQuery(request);

            /*// initialize request
            Query query = Query.newBuilder()
                .setHost("127.0.0.1")
                .setPort(27017)
                .setCollection("region_county")
                .setQuery(UNITED_STATES_QUERY)
                .build();

            CompoundRequest request = CompoundRequest.newBuilder()
                .setFirstQuery(query)
                .build();

            // send request
            Iterator<CompoundResponse> iterator =
                blockingStub.compoundQuery(request);*/

            int count = 0;
            while (iterator.hasNext()) {
                //CompoundResponse response = iterator.next();
                DirectResponse response = iterator.next();

                // print response
                //System.out.println(response.getJson());
                count += 1;
            }

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("retrieved " + count
                + " documents in " + duration + "ms");

            channel.shutdownNow();
        }
    }
}
