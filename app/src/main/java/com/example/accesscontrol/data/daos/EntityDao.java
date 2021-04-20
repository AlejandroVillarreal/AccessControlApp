package com.example.accesscontrol.data.daos;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

public abstract class EntityDao<E> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(E entity);
}
