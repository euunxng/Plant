package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IconlistDto {

    @Builder.Default
    private boolean rabbit=false;
    @Builder.Default
    private int seed=0;
    @Builder.Default
    private boolean stone=false;
    @Builder.Default
    private int energy1=0;
    @Builder.Default
    private int energy2=0;
    @Builder.Default
    private int energy3=0;
    @Builder.Default
    private boolean cuteStone=false;
    @Builder.Default
    private boolean ladybug=false;
    @Builder.Default
    private boolean title=false;
    @Builder.Default
    private boolean bfly=false;
}
