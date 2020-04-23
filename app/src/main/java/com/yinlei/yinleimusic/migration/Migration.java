package com.yinlei.yinleimusic.migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * 数据库的迁移
 */
public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();
        /**
         * 第一次迁移
         */
        if(oldVersion == 0){
            /**
             *  private String musicId;
             *     private String name;
             *     private String poster;
             *     private String path;
             *     private String author;
             */
            schema.create("MusicModel")
                    .addField("musicId", String.class)
                    .addField("name", String.class)
                    .addField("poster", String.class)
                    .addField("path", String.class)
                    .addField("author", String.class);
            /**
             * private String  albumId;
             *     private String  name;
             *     private String  poster;
             *     private String  playNum;
             *     private RealmList<MusicModel> list;
             */
            schema.create("AlbumModel")
                    .addField("albumId",String.class)
                    .addField("name",String.class)
                    .addField("poster",String.class)
                    .addField("playNum",String.class)
                    .addRealmListField("list", schema.get("MusicModel"));

            /**
             *  private RealmList<AlbumModel> album;
             *     private RealmList<MusicModel> hot;
             */
            schema.create("MusicSourceModel")
                    .addRealmListField("album",  schema.get("AlbumModel"))
                    .addRealmListField("hot",  schema.get("MusicModel"));
            oldVersion = newVersion;

        }
    }
}
