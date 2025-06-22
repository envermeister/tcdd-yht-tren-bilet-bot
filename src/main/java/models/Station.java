package models;

    public class Station {
        private int id; // Station ID
        private String name; // Station Name
        private String cityName; // City Name

        public Station(int id, String name, String cityName) {
            this.id = id;
            this.name = name;
            this.cityName = cityName;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getCityName() { return cityName; }

        public void setId(int id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setCityName(String cityName) { this.cityName = cityName; }
    }