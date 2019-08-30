package in.aks.searchonwikipedia.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashish on 9/28/2018.
 */

public class Page {
    private int pageId, ns, width, index, height;
    private String title, source, litleDescription;
    private JSONObject jsonObject;

    public Page(JSONObject jsonObject) throws JSONException {
        this.jsonObject = jsonObject;

        this.pageId = jsonObject.getInt("pageid");
        this.pageId = jsonObject.getInt("ns");
        this.title = jsonObject.getString("title");
        this.pageId = jsonObject.getInt("index");
        JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
        if (thumbnail!=null) {
            this.source = thumbnail.getString("source");
            this.width = thumbnail.getInt("width");
            this.height = thumbnail.getInt("height");
        }
        JSONObject terms = jsonObject.getJSONObject("terms");
        JSONArray description = terms.getJSONArray("description");
        if (description!=null) {
            for (int i = 0; i < description.length(); i++) {
                this.litleDescription = description.getString(i);
            }
        }
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
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
