package com.aar.app.wordsearch.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdularis on 08/07/17.
 *
 * Base class for type mapper
 */

public abstract class Mapper<From, To> {

    public abstract To map(From obj);

    public abstract From revMap(To obj);

    public List<To> map(List<From> objs) {
        if (objs == null) return null;

        List<To> result = new ArrayList<>();
        for (From obj : objs)
            result.add(map(obj));

        return result;
    }

    public List<From> revMap(List<To> objs) {
        if (objs == null) return null;

        List<From> result = new ArrayList<>();
        for (To obj : objs)
            result.add(revMap(obj));

        return result;
    }

}
