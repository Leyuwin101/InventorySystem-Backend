package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.model.entity.RefreshToken;
import com.example.inventorysystembackend.model.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user, String deviceInfo);

    RefreshToken validate(String token);

    void revokeToken(RefreshToken token);
}
