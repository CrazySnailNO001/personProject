package com.xzh.personalproject.commons.utils;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key) {
        //return (String)get(key);
        return SafeUtils.getString(get(key));
    }

    public boolean getBoolean(Object key) {
        boolean tmpret = false;
        if (key != null) {
            try {
                tmpret = Boolean.parseBoolean(get(key).toString());
            } catch (Exception ex) {
            }
        }
        return tmpret;
    }

    public int getInt(Object key) {
        return SafeUtils.getInt(get(key));
    }

    public int getInt(Object key, int val) {
        return SafeUtils.getInt(get(key), val);
    }

    public long getLong(Object key) {
        return SafeUtils.getLong(get(key));
    }

    public double getDouble(Object key) {
        return SafeUtils.getDouble(get(key));
    }

    public double getDouble(Object key, double val) {
        return SafeUtils.getDouble(get(key), val);
    }

    public BigDecimal getBigDecimal(Object key) {
        return new BigDecimal(SafeUtils.getString(get(key)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        map.putAll(t);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    public static String[] getStringArrayFromPageDataList(List<PageData> pageDataList, String columnName) {
        if (pageDataList == null) {
            return new String[]{};
        } else {
            List<String> ids = new ArrayList<>();
            for (PageData p : pageDataList) {
                ids.add(p.getString(columnName));
            }
            return ids.toArray(new String[pageDataList.size()]);
        }
    }

    public static List<Long> getLongListFromPageDataList(List<PageData> pageDataList, String columnName) {
        if (pageDataList == null) {
            return new ArrayList<>();
        } else {
            List<Long> ids = new ArrayList<>();
            for (PageData p : pageDataList) {
                ids.add(p.getLong(columnName));
            }
            return ids;
        }
    }

}
