package in.cioc.searchonwikipedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashish on 9/28/2018.
 */

public class Page {
    public int pageid, ns, width, index,height;
    public String title, source, litleDescription;
    JSONObject jsonObject;

    public Page(JSONObject jsonObject) throws JSONException {
        this.jsonObject = jsonObject;

        this.pageid = jsonObject.getInt("pageid");
        this.pageid = jsonObject.getInt("ns");
        this.title = jsonObject.getString("title");
        this.pageid = jsonObject.getInt("index");
        JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
        this.source = thumbnail.getString("source");
        this.width = thumbnail.getInt("width");
        this.height = thumbnail.getInt("height");
        JSONObject terms = jsonObject.getJSONObject("terms");
        JSONArray description = terms.getJSONArray("description");
        for (int i=0; i<description.length(); i++){
            this.litleDescription = description.getString(i);
        }
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLitleDescription() {
        return litleDescription;
    }

    public void setLitleDescription(String litleDescription) {
        this.litleDescription = litleDescription;
    }
}
