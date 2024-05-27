package org.myungkeun.auth_flow.controller;

import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.dto.request.PaginationRequest;
import org.myungkeun.auth_flow.dto.request.RegisterAddressRequest;
import org.myungkeun.auth_flow.dto.request.UpdateAddressRequest;
import org.myungkeun.auth_flow.dto.response.AllAddressResponse;
import org.myungkeun.auth_flow.dto.response.BaseResponse;
import org.myungkeun.auth_flow.entity.Address;
import org.myungkeun.auth_flow.exception.NotFoundException;
import org.myungkeun.auth_flow.service.AddressService;
import org.myungkeun.auth_flow.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Address>> registerAddress(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody RegisterAddressRequest request) {
        try {
            Address result = addressService.registerAddress(memberId, request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(result, HttpStatus.CREATED.value(), "주소 등록이 완료되었습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Address>> getAddressById(
            @PathVariable(name = "id") Long id
    ) {
        try {
            Address result = addressService.getAddressById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "주소를 불러왔습니다"));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Address>> updateAddressById(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateAddressRequest request
    ) {
        try {
            Address result = addressService.updateAddressById(id, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "주소를 수정 하였습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteAddressById(
            @PathVariable(name = "id") Long id
    ) {
        try {
            String result = addressService.deleteAddressById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "주소를 삭제 하였습니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<AllAddressResponse>> getAllAddress(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        try {
            PaginationRequest request = PaginationRequest.builder()
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .sortBy(sortBy)
                    .sortDir(sortDir)
                    .build();
            AllAddressResponse result = addressService.getAllAddress(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "주소를 모두 불러옵니다."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<BaseResponse<AllAddressResponse>> getAllAddressByMemberId(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        try {
            PaginationRequest request = PaginationRequest.builder()
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .sortBy(sortBy)
                    .sortDir(sortDir)
                    .build();
            AllAddressResponse result = addressService.getAllAddressByMemberId(memberId, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(result, HttpStatus.OK.value(), "해당 유저의 주소를 모두 불러옵니다."));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(null, HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}
