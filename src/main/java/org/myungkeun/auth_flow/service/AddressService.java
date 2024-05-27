package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.PaginationRequest;
import org.myungkeun.auth_flow.dto.request.RegisterAddressRequest;
import org.myungkeun.auth_flow.dto.request.UpdateAddressRequest;
import org.myungkeun.auth_flow.dto.response.AllAddressResponse;
import org.myungkeun.auth_flow.entity.Address;

public interface AddressService {
    Address registerAddress(Long memberId, RegisterAddressRequest request);

    Address getAddressById(Long id);

    Address updateAddressById(Long id, UpdateAddressRequest request);

    String deleteAddressById(Long id);

    AllAddressResponse getAllAddress(PaginationRequest request);
    AllAddressResponse getAllAddressByMemberId(Long memberId, PaginationRequest request);
}
