package com.fti.softi.services;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    @Autowired
  private CurrentUserService currentUserService;
  
  protected long getCurrentUserId(){ 
    return currentUserService.getCurrentUserId(); 
  }
}
