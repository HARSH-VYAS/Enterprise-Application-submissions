package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChronicleMapCache implements CacheInterface {

    private final Map<Long, Entry> cmp;

    public ChronicleMapCache() throws IOException {

        String pathname ="serverbindatafile.dat";
        File file=new File(pathname);
        Map<Long, Entry> map= ChronicleMapBuilder.of(Long.class, Entry.class).createPersistedTo(file);
        cmp=map;
    }
    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance must not be null");
        cmp.putIfAbsent(newEntry.getKey(),newEntry);
        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
        //Entry using=new Entry();
        return cmp.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(cmp.values());
    }



}
