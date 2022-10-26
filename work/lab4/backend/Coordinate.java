public class Coordinate {

    private final float latitude;
    private final float longitude;

    private Coordinate(UserBuilder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }

    public static class UserBuilder
    {
        private float latitude;
        private float longitude;

        public UserBuilder withLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }
        public UserBuilder withLongitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Coordinate build() {
            return  new Coordinate(this);
        }
    }
}