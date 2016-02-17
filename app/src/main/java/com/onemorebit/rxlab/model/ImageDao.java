package com.onemorebit.rxlab.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * Created by Euro on 2/15/16 AD.
 */
public class ImageDao {

    @SerializedName("success") public boolean success;

    /**
     * link : http://500px.com/photo/140390049/eiffel-tower-at-night-by-walter-casu
     * image_url : https://drscdn.500px.org/photo/140390049/m%3D1080_k%3D1_a%3D1/ec3cce9827b10d8f56c34e9420c75339
     * caption : Eiffel tower at night
     * user_id : 15816871
     * username : WalterCasu
     * profile_picture : https://graph.facebook.com/1096693513695671/picture?height=100&width=100
     * tags : ["France","Paris","Night","Lights","Eiffel Tower","Urban scape"]
     * created_time : 2016-02-15T04:55:41-05:00
     * camera : null
     * lens : null
     * focal_length : null
     * iso : null
     * shutter_speed : null
     * aperture : null
     */

    @SerializedName("data") public List<DataEntity> data;

    public static class DataEntity {
        @SerializedName("link") public String link;
        @SerializedName("image_url") public String imageUrl;
        @SerializedName("caption") public String caption;
        @SerializedName("user_id") public int userId;
        @SerializedName("username") public String username;
        @SerializedName("profile_picture") public String profilePicture;
        @SerializedName("created_time") public Date createdTime;
        @SerializedName("camera") public String camera;
        @SerializedName("lens") public String lens;
        @SerializedName("focal_length") public String focalLength;
        @SerializedName("iso") public String iso;
        @SerializedName("shutter_speed") public String shutterSpeed;
        @SerializedName("aperture") public String aperture;
        @SerializedName("tags") public List<String> tags;

        @Override public String toString() {
            return "DataEntity{" +
                "link='" + link + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", caption='" + caption + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", createdTime=" + createdTime +
                ", camera='" + camera + '\'' +
                ", lens='" + lens + '\'' +
                ", focalLength='" + focalLength + '\'' +
                ", iso='" + iso + '\'' +
                ", shutterSpeed='" + shutterSpeed + '\'' +
                ", aperture='" + aperture + '\'' +
                ", tags=" + tags +
                '}';
        }
    }
}
