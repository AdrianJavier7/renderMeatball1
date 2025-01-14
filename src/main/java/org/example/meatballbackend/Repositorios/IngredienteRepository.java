package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {


}
