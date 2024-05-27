package org.myungkeun.auth_flow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.PaginationRequest;
import org.myungkeun.auth_flow.dto.request.RegisterAddressRequest;
import org.myungkeun.auth_flow.dto.request.UpdateAddressRequest;
import org.myungkeun.auth_flow.dto.response.AllAddressResponse;
import org.myungkeun.auth_flow.entity.Address;
import org.myungkeun.auth_flow.entity.Member;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.repository.AddressRepository;
import org.myungkeun.auth_flow.repository.MemberRepository;
import org.myungkeun.auth_flow.service.AddressService;
import org.myungkeun.auth_flow.util.CacheManager;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;
    private final CacheManager cacheManager;

    @Override
    public Address registerAddress(Long memberId, RegisterAddressRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버를 찾을 수 없습니다"));
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipcode())
                .member(member)
                .build();
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 주소를 찾을 수 없습니다."));
    }

    @Override
    public Address updateAddressById(Long id, UpdateAddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 주소를 찾을 수 없습니다."));
        boolean isUpdated = false;
        if (request.getStreet() != null && !request.getStreet().equals(address.getStreet())) {
            address.setStreet(request.getStreet());
            isUpdated = true;
        }
        if (request.getCity() != null && !request.getCity().equals(address.getCity())) {
            address.setCity(request.getCity());
            isUpdated = true;
        }
        if (request.getState() != null && !request.getState().equals(address.getState())) {
            address.setState(request.getState());
            isUpdated = true;
        }
        if (request.getZipCode() != null && !request.getZipCode().equals(address.getZipCode())) {
            address.setZipCode(request.getZipCode());
            isUpdated = true;
        }
        if (isUpdated) {
            return addressRepository.save(address);
        } else {
            return address;
        }
    }

    @Override
    public String deleteAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 주소를 찾을 수 없습니다."));
        addressRepository.delete(address);
        return "deleted";
    }

    @Override
    public AllAddressResponse getAllAddress(PaginationRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageSize(), sort);
        Page<Address> addresses = addressRepository.findAll(pageable);
        return AllAddressResponse.builder()
                .pageNo(addresses.getNumber())
                .pageSize(addresses.getSize())
                .totalPages(addresses.getTotalPages())
                .totalElements(addresses.getTotalElements())
                .last(addresses.isLast())
                .content(addresses.getContent())
                .build();
    }

    @Override
    public AllAddressResponse getAllAddressByMemberId(Long memberId, PaginationRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버를 찾을 수 없습니다."));
        Sort sort = request.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageSize(), sort);
        List<Address> addressRepositoryAllByMember = addressRepository.findAllByMember(member);
        Page<Address> addresses = new PageImpl<>(addressRepositoryAllByMember, pageable, addressRepositoryAllByMember.size());
        return AllAddressResponse.builder()
                .pageNo(addresses.getNumber())
                .pageSize(addresses.getSize())
                .totalPages(addresses.getTotalPages())
                .totalElements(addresses.getTotalElements())
                .last(addresses.isLast())
                .content(addresses.getContent())
                .build();
    }
}
