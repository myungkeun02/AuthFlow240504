package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
