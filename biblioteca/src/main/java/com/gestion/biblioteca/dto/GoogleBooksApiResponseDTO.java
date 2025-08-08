package com.gestion.biblioteca.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleBooksApiResponseDTO {

    private List<VolumeDTO> items;

    public VolumeInfoDTO getFirstVolumeInfo() {
        return items != null && !items.isEmpty() ? items.get(0).getVolumeInfo() : null;
    }
}
