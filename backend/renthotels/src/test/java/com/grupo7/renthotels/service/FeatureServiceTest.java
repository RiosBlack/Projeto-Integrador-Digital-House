package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Feature;
import com.grupo7.renthotels.repository.FeatureRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FeatureServiceTest {

    @Mock
    private FeatureRepository featureRepository;
    
    @InjectMocks
    private FeatureService featureService;

    @Test
    public void shouldGetAFeature() throws NotFoundException {
        Feature feat = Feature.builder()
                .featureTitle("Spa")
                .featureIconUrl("IconSpa")
                .build();
        feat.setFeatureSku(1234567L);

        when(featureRepository.findByFeatureSku(1234567L)).thenReturn(Optional.of(feat));

        var result = featureService.getAFeature(1234567L);
        assertThat(result).hasFieldOrPropertyWithValue("featureTitle", "Spa");
  }

    @Test
    public void shouldNotGetAFeatureWithAnInexistingSku() {

        when(featureRepository.findByFeatureSku(1234567L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> featureService.getAFeature(1234567L));
    }

    @Test
    public void shouldReturnPremiumFeatures() throws NotFoundException {
        Feature feat1 = new Feature();
        feat1.setFeatureSku(7694639L);
        Feature feat2 = new Feature();
        feat2.setFeatureSku(3578694L);
        Feature feat3 = new Feature();
        feat3.setFeatureSku(1597239L);
        Feature feat4 = new Feature();
        feat4.setFeatureSku(8796841L);
        Feature feat5 = new Feature();
        feat5.setFeatureSku(8745741L);

        when(featureRepository.findByFeatureSku(7694639L)).thenReturn(Optional.of(feat1));
        when(featureRepository.findByFeatureSku(3578694L)).thenReturn(Optional.of(feat2));
        when(featureRepository.findByFeatureSku(1597239L)).thenReturn(Optional.of(feat3));
        when(featureRepository.findByFeatureSku(8796841L)).thenReturn(Optional.of(feat4));
        when(featureRepository.findByFeatureSku(8745741L)).thenReturn(Optional.of(feat5));

        List<Feature> expectedFeats = List.of(feat1, feat2, feat3, feat4, feat5);

        List<Feature> result = featureService.premiumFeatures();

        assertEquals(expectedFeats, result);
    }

    @Test
    public void shouldReturnMasterFeatures() throws NotFoundException {
        Feature feat1 = new Feature();
        feat1.setFeatureSku(7694639L);
        Feature feat2 = new Feature();
        feat2.setFeatureSku(3578694L);
        Feature feat3 = new Feature();
        feat3.setFeatureSku(1597239L);
        Feature feat4 = new Feature();
        feat4.setFeatureSku(8796841L);
        Feature feat5 = new Feature();
        feat5.setFeatureSku(8745741L);
        Feature feat6 = new Feature();
        feat6.setFeatureSku(9856327L);

        when(featureRepository.findByFeatureSku(7694639L)).thenReturn(Optional.of(feat1));
        when(featureRepository.findByFeatureSku(3578694L)).thenReturn(Optional.of(feat2));
        when(featureRepository.findByFeatureSku(1597239L)).thenReturn(Optional.of(feat3));
        when(featureRepository.findByFeatureSku(8796841L)).thenReturn(Optional.of(feat4));
        when(featureRepository.findByFeatureSku(8745741L)).thenReturn(Optional.of(feat5));
        when(featureRepository.findByFeatureSku(9856327L)).thenReturn(Optional.of(feat6));

        List<Feature> result = featureService.masterFeatures();

        assertTrue(result.contains(feat6));
    }

    @Test
    public void shouldReturnDeluxFeatures() throws NotFoundException {
        Feature feat1 = new Feature();
        feat1.setFeatureSku(7694639L);
        Feature feat2 = new Feature();
        feat2.setFeatureSku(3578694L);
        Feature feat3 = new Feature();
        feat3.setFeatureSku(1597239L);
        Feature feat4 = new Feature();
        feat4.setFeatureSku(8796841L);
        Feature feat5 = new Feature();
        feat5.setFeatureSku(8745741L);
        Feature feat6 = new Feature();
        feat6.setFeatureSku(9856327L);
        Feature feat7 = new Feature();
        feat7.setFeatureSku(1678894L);

        when(featureRepository.findByFeatureSku(7694639L)).thenReturn(Optional.of(feat1));
        when(featureRepository.findByFeatureSku(3578694L)).thenReturn(Optional.of(feat2));
        when(featureRepository.findByFeatureSku(1597239L)).thenReturn(Optional.of(feat3));
        when(featureRepository.findByFeatureSku(8796841L)).thenReturn(Optional.of(feat4));
        when(featureRepository.findByFeatureSku(8745741L)).thenReturn(Optional.of(feat5));
        when(featureRepository.findByFeatureSku(9856327L)).thenReturn(Optional.of(feat6));
        when(featureRepository.findByFeatureSku(1678894L)).thenReturn(Optional.of(feat7));

        List<Feature> result = featureService.deluxFeatures();

        assertEquals(7, result.size());
    }

    @Test
    public void shouldReturnEurostarFeatures(){
        Feature feat1 = new Feature();
        feat1.setFeatureSku(7694639L);
        Feature feat2 = new Feature();
        feat2.setFeatureSku(3578694L);
        Feature feat3 = new Feature();
        feat3.setFeatureSku(1597239L);
        Feature feat4 = new Feature();
        feat4.setFeatureSku(8796841L);
        Feature feat5 = new Feature();
        feat5.setFeatureSku(8745741L);
        Feature feat6 = new Feature();
        feat6.setFeatureSku(9856327L);
        Feature feat7 = new Feature();
        feat7.setFeatureSku(1678894L);
        Feature feat8 = new Feature();
        feat8.setFeatureSku(4568727L);
        Feature feat9 = new Feature();
        feat9.setFeatureSku(2878994L);
        Feature feat10 = new Feature();
        feat10.setFeatureSku(8123441L);

        when(featureRepository.findByFeatureSku(7694639L)).thenReturn(Optional.of(feat1));
        when(featureRepository.findByFeatureSku(3578694L)).thenReturn(Optional.of(feat2));
        when(featureRepository.findByFeatureSku(1597239L)).thenReturn(Optional.of(feat3));
        when(featureRepository.findByFeatureSku(8796841L)).thenReturn(Optional.of(feat4));
        when(featureRepository.findByFeatureSku(8745741L)).thenReturn(Optional.of(feat5));
        when(featureRepository.findByFeatureSku(9856327L)).thenReturn(Optional.of(feat6));
        when(featureRepository.findByFeatureSku(1678894L)).thenReturn(Optional.of(feat7));
        when(featureRepository.findByFeatureSku(4568727L)).thenReturn(Optional.of(feat8));
        when(featureRepository.findByFeatureSku(2878994L)).thenReturn(Optional.of(feat9));
        when(featureRepository.findByFeatureSku(8123441L)).thenReturn(Optional.of(feat10));

        List<Feature> expectedFeats = List.of(feat1, feat2, feat3, feat4, feat5, feat6, feat7, feat8, feat9, feat10);

        when(featureRepository.findAll()).thenReturn((expectedFeats));

        List<Feature> result = featureService.eurostarFeatures();

        assertEquals(10, result.size());
    }
}