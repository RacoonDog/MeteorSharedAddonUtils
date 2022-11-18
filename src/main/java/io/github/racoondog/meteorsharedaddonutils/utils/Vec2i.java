package io.github.racoondog.meteorsharedaddonutils.utils;

import com.google.common.base.MoreObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A vector composed of 2 integers.
 * {@link Vec2i.Immutable} for an immutable implementation.
 * {@link Vec2i.Mutable} for a mutable implementation.
 */
@Environment(EnvType.CLIENT)
public interface Vec2i {
    int getX();
    int getY();
    Vec2i set(int x, int y);
    Vec2i setX(int x);
    Vec2i setY(int y);

    Vec2i add(int x, int y);
    Vec2i add(Vec2i vec2i);
    Vec2i add(int value);
    Vec2i subtract(int x, int y);
    Vec2i subtract(Vec2i vec2i);
    Vec2i subtract(int value);
    Vec2i multiply(int scale);
    Vec2i negate();

    default int compareTo(Vec2i vec2i) {
        if (this.getX() == vec2i.getX()) {
            return this.getY() - vec2i.getY();
        }
        return this.getX() - vec2i.getX();
    }

    default String toShortString() {
        return this.getX() + ", " + this.getY();
    }

    default boolean equals(Vec2i other) {
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    class Immutable implements Vec2i {
        public final int x, y;

        public Immutable(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public Vec2i set(int x, int y) {
            return new Vec2i.Immutable(x, y);
        }

        @Override
        public Vec2i setX(int x) {
            return new Vec2i.Immutable(x, this.y);
        }

        @Override
        public Vec2i setY(int y) {
            return new Vec2i.Immutable(this.x, y);
        }

        @Override
        public Vec2i add(int x, int y) {
            return new Vec2i.Immutable(this.x + x, this.y + y);
        }

        @Override
        public Vec2i add(Vec2i vec2i) {
            return new Vec2i.Immutable(this.x + vec2i.getX(), this.y + vec2i.getY());
        }

        @Override
        public Vec2i add(int value) {
            return new Vec2i.Immutable(this.x + value, this.y + value);
        }

        @Override
        public Vec2i subtract(int x, int y) {
            return new Vec2i.Immutable(this.x - x, this.y - y);
        }

        @Override
        public Vec2i subtract(Vec2i vec2i) {
            return new Vec2i.Immutable(this.x - vec2i.getX(), this.y - vec2i.getY());
        }

        @Override
        public Vec2i subtract(int value) {
            return new Vec2i.Immutable(this.x - value, this.y - value);
        }

        @Override
        public Vec2i multiply(int scale) {
            return new Vec2i.Immutable(this.x * scale, this.y * scale);
        }

        @Override
        public Vec2i negate() {
            return new Vec2i.Immutable(-this.x, -this.y);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("x", this.x).add("y", this.y).toString();
        }
    }

    class Mutable implements Vec2i {
        public int x, y;

        public Mutable() {

        }

        public Mutable(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public Vec2i set(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public Vec2i setX(int x) {
            this.x = x;
            return this;
        }

        @Override
        public Vec2i setY(int y) {
            this.y = y;
            return this;
        }

        @Override
        public Vec2i add(int x, int y) {
            this.x += x;
            this.y += y;
            return this;
        }

        @Override
        public Vec2i add(Vec2i vec2i) {
            this.x += vec2i.getX();
            this.y += vec2i.getY();
            return this;
        }

        @Override
        public Vec2i add(int value) {
            this.x += value;
            this.y += value;
            return this;
        }

        @Override
        public Vec2i subtract(int x, int y) {
            this.x -= x;
            this.y -= y;
            return this;
        }

        @Override
        public Vec2i subtract(Vec2i vec2i) {
            this.x -= vec2i.getX();
            this.y -= vec2i.getY();
            return this;
        }

        @Override
        public Vec2i subtract(int value) {
            this.x -= value;
            this.y -= value;
            return this;
        }

        @Override
        public Vec2i multiply(int scale) {
            this.x *= scale;
            this.y *= scale;
            return this;
        }

        @Override
        public Vec2i negate() {
            this.x = -x;
            this.y = -y;
            return this;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("x", this.x).add("y", this.y).toString();
        }
    }
}
