package org.myungkeun.auth_flow.repository;

import org.myungkeun.auth_flow.entity.Address;
import org.myungkeun.auth_flow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByMember(Member member);
}
