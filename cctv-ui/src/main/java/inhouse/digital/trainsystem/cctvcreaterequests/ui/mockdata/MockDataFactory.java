package inhouse.digital.trainsystem.cctvcreaterequests.ui.mockdata;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import java.util.UUID;

public final class MockDataFactory {

    private MockDataFactory() {
    }

    private static final ImmutableList<MockJourney> journeys;
    private static final ImmutableList<MockTrain> trains;

    private static final UnifiedMap<UUID, ImmutableList<MockTrain>> trainsForJourneyId;

    static {
        journeys = Lists.immutable.of(
                new MockJourney(
                        UUID.fromString("5578f8d4-3d4e-46b7-978b-2acdaccfcd6e"),
                        "1A00 [From Newcastle at 04:57:00 to Acklington at 05:21:30]"),

                new MockJourney(
                        UUID.fromString("fb0baee5-af69-4cc2-ab1f-58d57da02bfa"),
                        "1B00 [From Blackpool North at 21:02:00 to Bramley; W Yorks at 22:50:30]")
        );

        trains = Lists.immutable.of(
                new MockTrain(
                  UUID.fromString("2e3a7dc7-b25c-4ff5-8d69-d62a326cfab4"),
                  "156 (2 Car) [162]"
                ),
                new MockTrain(
                        UUID.fromString("4949d279-8086-4a80-97ac-3eac080f232b"),
                        "157 (3 Car) [163]"
                ),
                new MockTrain(
                        UUID.fromString("8371e45a-ee94-4a25-b516-c7e99042501b"),
                        "158 (4 Car) [164]"
                ),
                new MockTrain(
                        UUID.fromString("67e83c5a-c83f-47d2-8a63-34fc65f66ed9"),
                        "159 (5 Car) [165]"
                )
        );

        trainsForJourneyId = new UnifiedMap<>(2);

        trainsForJourneyId.withKeysValues(
                journeys.get(0).id(),
                Lists.immutable.of(trains.get(0), trains.get(1)));

        trainsForJourneyId.withKeysValues(
                journeys.get(1).id(),
                Lists.immutable.of(trains.get(2), trains.get(3)));
    }


    /**
     * Format of the journey name will be
     * {Headcode} [{From Location} {hour} -> {To Location} {hour}]
     * Example: 1A00 [From Newcastle at 04:57:00 to Acklington at 05:21:30]
     * 1B00 [From Blackpool North at 21:02:00 to Bramley; W Yorks at 22:50:30]
     */
    public static ImmutableList<String> getJourneyNamesForDropdown() {
        return journeys.collect(MockJourney::displayName);
    }

    /**
     * Example: 156 (2 Car) [162] - 156468 I don't know what the data is exactly
     *
     * @return
     */
    public static ImmutableList<String> getTrainNamesForDropdown(UUID journeyId) {
        return trainsForJourneyId
                .get(journeyId)
                .collect(MockTrain::code);
    }
}
