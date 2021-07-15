package com.ederson.beerstock.builder;

import com.ederson.beerstock.dto.BeerDTO;
import com.ederson.beerstock.enums.BeerType;

public class BeerDTOBuilder {
    private Long id;
    private String name;
    private String brand;
    private int max;
    private int quantity;
    private BeerType type;

    public BeerDTO toBeerDTO() {
        return new BeerDTO(this.id, this.name, this.brand, this.max, this.quantity, this.type);
    }

    private static Long $default$id() {
        return 1L;
    }

    private static String $default$name() {
        return "Brahma";
    }

    private static String $default$brand() {
        return "Ambev";
    }

    private static int $default$max() {
        return 50;
    }

    private static int $default$quantity() {
        return 10;
    }

    private static BeerType $default$type() {
        return BeerType.LAGER;
    }

    BeerDTOBuilder(final Long id, final String name, final String brand, final int max, final int quantity, final BeerType type) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.max = max;
        this.quantity = quantity;
        this.type = type;
    }

    public static BeerDTOBuilder.BeerDTOBuilderBuilder builder() {
        return new BeerDTOBuilder.BeerDTOBuilderBuilder();
    }

    public static class BeerDTOBuilderBuilder {
        private boolean id$set;
        private Long id$value;
        private boolean name$set;
        private String name$value;
        private boolean brand$set;
        private String brand$value;
        private boolean max$set;
        private int max$value;
        private boolean quantity$set;
        private int quantity$value;
        private boolean type$set;
        private BeerType type$value;

        BeerDTOBuilderBuilder() {
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder id(final Long id) {
            this.id$value = id;
            this.id$set = true;
            return this;
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder name(final String name) {
            this.name$value = name;
            this.name$set = true;
            return this;
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder brand(final String brand) {
            this.brand$value = brand;
            this.brand$set = true;
            return this;
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder max(final int max) {
            this.max$value = max;
            this.max$set = true;
            return this;
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder quantity(final int quantity) {
            this.quantity$value = quantity;
            this.quantity$set = true;
            return this;
        }

        public BeerDTOBuilder.BeerDTOBuilderBuilder type(final BeerType type) {
            this.type$value = type;
            this.type$set = true;
            return this;
        }

        public BeerDTOBuilder build() {
            Long id$value = this.id$value;
            if (!this.id$set) {
                id$value = BeerDTOBuilder.$default$id();
            }

            String name$value = this.name$value;
            if (!this.name$set) {
                name$value = BeerDTOBuilder.$default$name();
            }

            String brand$value = this.brand$value;
            if (!this.brand$set) {
                brand$value = BeerDTOBuilder.$default$brand();
            }

            int max$value = this.max$value;
            if (!this.max$set) {
                max$value = BeerDTOBuilder.$default$max();
            }

            int quantity$value = this.quantity$value;
            if (!this.quantity$set) {
                quantity$value = BeerDTOBuilder.$default$quantity();
            }

            BeerType type$value = this.type$value;
            if (!this.type$set) {
                type$value = BeerDTOBuilder.$default$type();
            }

            return new BeerDTOBuilder(id$value, name$value, brand$value, max$value, quantity$value, type$value);
        }

        public String toString() {
            return "BeerDTOBuilder.BeerDTOBuilderBuilder(id$value=" + this.id$value + ", name$value=" + this.name$value + ", brand$value=" + this.brand$value + ", max$value=" + this.max$value + ", quantity$value=" + this.quantity$value + ", type$value=" + this.type$value + ")";
        }
    }
}
