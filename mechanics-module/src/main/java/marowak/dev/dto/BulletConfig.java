package marowak.dev.dto;

import lombok.Getter;

/**
 * width, height 1 meter - 1 pixel
 * density kg/m2
 * mass kg
 * speed m/sed
 * restitution %
 */
@Getter
public class BulletConfig {
    int width;
    int height;
    double density;
    int speed;
    int restitution;
    double angularDamping;
    double linearDamping;
    double mass;

    public BulletConfig(int width, int height, double density, int speed, int restitution, double angularDamping,
                        double linearDamping) {
        this.width = width;
        this.height = height;
        this.density = density;
        this.speed = speed;
        this.restitution = restitution;
        this.angularDamping = angularDamping;
        this.linearDamping = linearDamping;
    }

    public double getMass() {
        return width * height * density;
    }
}
