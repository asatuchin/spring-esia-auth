package com.example.das_auth_providers.vk.entity.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vk_user_links")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Builder
public class VKUserLink {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true)
    private String email;
    @Column
    private String vkId;
}
