package pl.saletrak.wypokplus.layout_elements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EntryData extends PostData {

    int id;
    String author;
    int author_group;
    String time;
    String app;
    String avatar;
    String sex;

    String body;
    JSONObject embed;
    String embed_type;
    String embed_preview;
    String embed_url;
    boolean embed_plus18;
    String embed_source;

    JSONArray comments;
    JSONArray voters;
    boolean blocked;
    boolean deleted;

    int vote_count;
    int comment_count;
    int user_vote;
    boolean user_favorite;

    public EntryData(JSONObject jsonObject) {
        super(jsonObject);

        try {
            this.id = post_data.getInt("id");
            this.author = post_data.getString("author");
            this.author_group = post_data.getInt("author_group");
            this.time = post_data.getString("date");
            this.app = post_data.getString("app");
            this.avatar = post_data.getString("author_avatar");
            this.sex = post_data.getString("author_sex");

            this.body = post_data.getString("body");
            if(!post_data.get("embed").equals(null)) {
                this.embed = post_data.getJSONObject("embed");
                this.embed_type = this.embed.getString("type");
                this.embed_preview = this.embed.getString("preview");
                this.embed_url = this.embed.getString("url");
                this.embed_source = this.embed.getString("source");
                this.embed_plus18 = this.embed.getBoolean("plus18");
            }
            else this.embed = null;

            this.comments = post_data.getJSONArray("comments");
            this.voters = post_data.getJSONArray("voters");
            this.blocked = post_data.getBoolean("blocked");
            this.deleted = post_data.getBoolean("deleted");

            this.vote_count = post_data.getInt("vote_count");
            this.comment_count = post_data.getInt("comment_count");
            this.user_vote = post_data.getInt("user_vote");
            this.user_favorite = post_data.getBoolean("user_favorite");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
