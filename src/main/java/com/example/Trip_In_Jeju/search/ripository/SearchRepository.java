package com.example.Trip_In_Jeju.search.ripository;

import com.example.Trip_In_Jeju.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    @Query("SELECT s FROM Search s WHERE s.searchTerm = :searchTerm")
    List<Search> findBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT a.title FROM Activity a WHERE a.title LIKE %:title%")
    List<String> findByTitleContaining(@Param("title") String title);
}