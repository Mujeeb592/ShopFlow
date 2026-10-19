
package com.shopflow;

import java.util.HashSet;
import java.util.Set;

public class CustomerEqualityTest {

    public static void main(String[] args) {

        Customer first = new Customer(
                1, "Mujeeb", "mujeeb@example.com", "03001234567"
        );

        Customer second = new Customer(
                1, "Another Name", "another@example.com", "03111234567"
        );

        System.out.println("Are customers equal? " + first.equals(second));

        Set<Customer> customerSet = new HashSet<>();
        customerSet.add(first);
        customerSet.add(second);

        System.out.println("HashSet size: " + customerSet.size());
    }
}
