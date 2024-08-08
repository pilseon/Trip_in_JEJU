    package com.example.Trip_In_Jeju.event.repository;

    import com.example.Trip_In_Jeju.event.entity.Event;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface EventRepository extends JpaRepository<Event, Long> {
    }
