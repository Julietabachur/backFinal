package com.backendIntegrador.service.impl;

import com.backendIntegrador.DTO.ReportePpCDto;
import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Product;
import com.backendIntegrador.model.Reserve;
import com.backendIntegrador.repository.CategoryRepository;
import com.backendIntegrador.repository.ProductRepository;
import com.backendIntegrador.repository.ReserveRepository;
import com.backendIntegrador.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private final ProductIdService productIdService = null;

    @Autowired
    public ProductService( ProductRepository productRepository ) {
        this.productRepository = productRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    @Transactional
    public Product save( Product product ) throws Exception {
        try {
            Long generatedProductId = productIdService.getNextSequence("product");
            product.setProductId(generatedProductId);
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public Page<Product> productList( Pageable pageable ) throws Exception {
        try {
            return productRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<Product> findByIdIn( List<String> productId,Pageable pageable ) {
        return productRepository.findByIdIn(productId, pageable);
    }


    @Override
    public Product update( Product product ) throws Exception {
        try {
            Product existingProduct = productRepository.findById(product.getId()).orElse(null);

            if (existingProduct != null) {
                Product updatedProduct = productRepository.save(product);
                return updatedProduct;
            } else {
                throw new RuntimeException("El producto no se encontró para la actualización");
            }

            // Actualiza el producto en la base de datos


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<Product> findByCategoryIn( Pageable pageable, List<String> categories ) {
        return productRepository.findByCategoryIn(pageable, categories);
    }

    @Override
    public Page<Product> searchProductsByProductNameAndDateRange(
            String productName, LocalDate startDate, LocalDate endDate, PageRequest pageable) {
        try {
            List<String> availableProductIds = new ArrayList<>();

            // Obtener productos por nombre
            List<Product> products = productRepository.findByProductNameRegexIgnoreCase(productName);

            // Verificar superposición de fechas
            for (Product product : products) {

                boolean isAvailable = isProductAvailable(product.getId(),startDate,endDate);

                // Obtener reservas asociadas al producto
                List<Reserve> reservations = reserveRepository.findByIdIn(product.getReserveIds());

                // Verificar superposición de fechas con cada reserva
                /*for (Reserve reservation : reservations) {
                    if (isDateRangeOverlap(reservation.getStartDate(), reservation.getEndDate(), startDate, endDate)) {
                        isAvailable = false;
                        break; // No es necesario verificar más reservas si ya hay superposición
                    }
                }

                 */

                // Si no hay superposición, agregar el producto a la lista de disponibles
                if (isAvailable) {
                    availableProductIds.add(product.getId());
                }
            }

            // Obtener los productos disponibles paginados
            return productRepository.findByIdIn(availableProductIds,pageable);
        } catch (Exception e) {
            // Manejar excepciones según tus necesidades
            throw new RuntimeException("Error al buscar productos disponibles: " + e.getMessage());
        }
    }

    // Método para verificar si el producto está disponible en la fecha proporcionada
    private boolean isProductAvailable(String productId, LocalDate startDate, LocalDate endDate) {
        // Consultar las reservas existentes para el producto
        List<Reserve> existingReservations = reserveRepository.findByProductId(productId);

        // Verificar si la fecha inicial está dentro del rango de fechas de alguna reserva
        for (Reserve existingReserve : existingReservations) {
            LocalDate reserveStartDate = existingReserve.getStartDate();

            // Si la fecha inicial está dentro del rango de fechas de la reserva, el producto no está disponible
            if (!startDate.isBefore(reserveStartDate)) {
                return false;
            }

        }

        return true; // El producto está disponible
    }

    // Método para devolver todos los productos segun busqueda en searchbar

    public Page<Product> searchProductsByProductName(String productName, PageRequest pageable) {
        try {
            List<String> availableProductIds = new ArrayList<>();

            // Obtener productos por nombre
            List<Product> products = productRepository.findByProductNameRegexIgnoreCase(productName);

            for (Product product : products) {
                availableProductIds.add(product.getId());

            }

            // Obtener los productos disponibles paginados
            return productRepository.findByIdIn(availableProductIds,pageable);
        } catch (Exception e) {
            // Manejar excepciones según tus necesidades
            throw new RuntimeException("Error al buscar productos disponibles: " + e.getMessage());
        }
    }

    public Page<Product> searchProductsBySeason(String season, PageRequest pageable) {
        try {
            List<String> availableProductIds = new ArrayList<>();

            // Obtener productos por temporada
            List<Product> products = productRepository.findBySeason(season);

            for (Product product : products) {
                availableProductIds.add(product.getId());

            }

            // Obtener los productos disponibles paginados
            return productRepository.findByIdIn(availableProductIds,pageable);
        } catch (Exception e) {
            // Manejar excepciones según tus necesidades
            throw new RuntimeException("Error al buscar productos disponibles: " + e.getMessage());
        }
    }


    @Override
    public Product getProductById( String id ) throws Exception {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public Page<Product> getAll( Pageable pageable ) {

        return productRepository.findAll(pageable);
    }

    @Override
    public Product checkProductName( String productName ) {
        return productRepository.checkProductName(productName);
    }

    public Page<Product> getProductsByCategoryNames(List<String> categoryNames, Pageable pageable) {
        return productRepository.findByCategoryNames(categoryNames, pageable);
    }

    /*public Map<String, Long> getProductCountByCategory() {
        // Ejecuta la consulta en el repositorio para contar productos por categoría
        List<Map<String, Object>> counts = productRepository.countProductsByCategory();

        // Transforma la lista de resultados en un mapa clave-valor (categoría -> cantidad)
        return counts.stream()
                .collect(Collectors.toMap(
                        result -> result.get("_id").toString(), // La categoría (clave)
                        result -> Long.valueOf(result.get("count").toString()) // La cantidad (valor)
                ));
    }*/

    public List<ReportePpCDto> getProductCountByCategory() {
        List<Product> allProduct = productRepository.findAll();
        List<Category> allCategory = categoryRepository.findAll();
        List<ReportePpCDto> allReport= new ArrayList<>();

        for (Category category:allCategory) {
            ReportePpCDto newReportDto = new ReportePpCDto();
            newReportDto.setName(category.getCategoryName());
            newReportDto.setAmount(0);
            allReport.add(newReportDto);
        }
        for (Product product:allProduct) {
            for (ReportePpCDto reportePpCDto:allReport){
                if (product.getCategory().equals(reportePpCDto.getName())) {
                    reportePpCDto.setAmount(reportePpCDto.getAmount()+1);
                }
            }
        }

        return allReport;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }


}
