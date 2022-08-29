import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class IF_Storage {
    private static final Map<LocalDateTime, SysSuper_Component.StorageData> STORED_DATA = new HashMap<>();

    protected static void addData(SysSuper_Component.StorageData storageData) {
        STORED_DATA.put(storageData.DATE_TIME, storageData);
    }

    protected static SysSuper_Component.StorageData[] getData(LocalDateTime start, LocalDateTime end) {
        AtomicInteger number = new AtomicInteger(0);
        STORED_DATA.forEach((k, v) ->  {
            if ((k.isEqual(start) || k.isAfter(start)) &&
                    (k.isEqual(end) || k.isBefore(end))) {
                number.getAndIncrement();
            }
        });
        SysSuper_Component.StorageData[] result = new SysSuper_Component.StorageData[number.get()];
        AtomicInteger i = new AtomicInteger(0);
        STORED_DATA.forEach((k, v) ->  {
            if ((k.isEqual(start) || k.isAfter(start)) &&
                    (k.isEqual(end) || k.isBefore(end))) {
                result[i.get()] = v;
                i.getAndIncrement();
            }
        });
        return result;
    }

    protected static void deleteOlderThanDays(int days) {
        LocalDateTime limit = LocalDateTime.now().minusDays(days);
        STORED_DATA.forEach((k, v) -> {
            if (k.isBefore(limit)) {
                STORED_DATA.remove(k);
            }
        });
    }

}
