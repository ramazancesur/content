package com.content_load_sb.helper;

/**
 * Created by asd on 8.11.2017.
 */
public class EnumUtil {
    public enum MaxUploadSize {
        TB, GB, MB, KB
    }


    public enum RunModeEnum {
        DELTA, INITIAL;
    }

    public enum FileSocket {
        YES, NO
    }

    public enum FileType{
        FILE,DIRECTORY
    }

    public enum DataBaseType{
        H2,MYSQL,MYSQL_SLAVE
    } 

    public enum EntityState {
        PASSIVE(0, "Pasif"), ACTIVE(1, "Aktif");
        private final Integer id;
        private final String name;


        private EntityState(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public static EntityState parse(Integer id) {
            for (EntityState entityState : EntityState.values()) {
                if (entityState.getId().equals(id)) {
                    return entityState;
                }
            }
            return null;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
