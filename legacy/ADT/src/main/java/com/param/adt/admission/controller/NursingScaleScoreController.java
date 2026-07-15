package com.param.adt.admission.controller;

import com.param.adt.admission.dto.*;
import com.param.adt.admission.service.INursingScaleScoreService;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/adt")
public class NursingScaleScoreController {

    @Autowired
    private INursingScaleScoreService iNursingScaleScoreService;

    @RequestMapping(value = "/getAllNursingScaleScore/{unitId}/{organizationId}/{admissionId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getAllNursingScaleScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("admissionId") Integer admissionId,
                                                          @PathVariable("encounterId") Integer encounterId) {
        try {
            return iNursingScaleScoreService.getAllNursingScaleScore(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAllNursingScoreById/{unitId}/{organizationId}/{admissionId}/{tabNumber}", method = RequestMethod.GET)
    public @ResponseBody Response getAllNursingScoreById(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("admissionId") Integer admissionId,
                                                          @PathVariable("tabNumber") Integer tabNumber) {
        try {
            return iNursingScaleScoreService.getAllNursingScoreById(unitId, organizationId, admissionId, tabNumber);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveGcsScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveGcsScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveGcsScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getTotalGcsScaleScoreById/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getGcsScaleScoreById(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                       @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getGcsScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveAsDraftGcsScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftGcsScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveAsDraftGcsScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAllAsDraftGcsScaleScore/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getAllAsDraftGcsScaleScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                             @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getAllAsDraftGcsScaleScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveApacheScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveApacheScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveApacheScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }

    }


    @RequestMapping(value = "/getApacheScaleScoreById/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getApacheScaleScoreById(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getApacheScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveAsDraftApacheScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftApacheScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveAsDraftApacheScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAllAsDraftApacheScaleScore/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getAllAsDraftApacheScaleScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getAllAsDraftApacheScaleScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveSofaScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveSofaScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveSofaScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSofaScaleScoreById/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getSofaScaleScoreById(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                        @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getSofaScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveAsDraftSofaScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftSofaScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveAsDraftSofaScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAllAsDraftSofaScaleScore/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getAllAsDraftSofaScaleScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getAllAsDraftSofaScaleScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveBradenScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveBradenScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveBradenScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getBradenScaleScoreById/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getBradenScaleScoreById(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getBradenScaleScoreById(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }


    @RequestMapping(value = "/saveAsDraftBradenScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftBradenScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveAsDraftBradenScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAllAsDraftBradenScaleScore/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getAllAsDraftBradenScaleScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNursingScaleScoreService.getAllAsDraftBradenScaleScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveNpuapScaleScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveNpuapScaleScore(@RequestBody GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveNpuapScaleScore(gcsScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveBishopScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveDraftedBishopScore(@RequestBody BishopScaleScoreDTO bishopScaleScoreDTO) {
        try {
            return iNursingScaleScoreService.saveDraftedBishopScore(bishopScaleScoreDTO);
        } catch (Exception e) {
            throw e;
        }

    }

    @RequestMapping(value = "/getDraftedBishopScore/{unitId}/{organizationId}/{admissionId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getDraftedBishopScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                @PathVariable("admissionId") Integer admissionId,
                                                                @PathVariable("encounterId") Integer encounterId) {
        try {
            return iNursingScaleScoreService.getDraftedBishopScore(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveAldreteScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAldreteScore(@RequestBody AldreteScaleScoreDto aldreteScaleScoreDto) {
        try {
            return iNursingScaleScoreService.saveAldreteScore(aldreteScaleScoreDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getDraftedAldreteScore/{unitId}/{organizationId}/{admissionId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getDraftedAldreteScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                        @PathVariable("admissionId") Integer admissionId,
                                                        @PathVariable("encounterId") Integer encounterId) {
        try {
            return iNursingScaleScoreService.getDraftedAldreteScore(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }
    @RequestMapping(value = "/saveAPGARScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAPGARScore(@RequestBody APGARScaleScoreDto apgarScaleScoreDto) {
        try {
            return iNursingScaleScoreService.saveApgarScore(apgarScaleScoreDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getDraftedAPGARScore/{unitId}/{organizationId}/{admissionId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getDraftedAPGARScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                         @PathVariable("admissionId") Integer admissionId,
                                                         @PathVariable("encounterId") Integer encounterId) {
        try {
            return iNursingScaleScoreService.getAPGARDraftScaleScores(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/saveMallampattiClass", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveMallampattiClass(@RequestBody MallamapattiClassDto mallamapattiClassDto) {
        try {
            return iNursingScaleScoreService.saveMallampattiClass(mallamapattiClassDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getDraftedMallampattiClass/{unitId}/{organizationId}/{admissionId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getDraftedMallampattiClass(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                       @PathVariable("admissionId") Integer admissionId,
                                                       @PathVariable("encounterId") Integer encounterId) {
        try {
            return iNursingScaleScoreService.getDraftedMallampattiClass(unitId, organizationId, admissionId, encounterId);
        } catch (Exception e) {
            throw e;
        }
    }
    
    
   
	@RequestMapping(value = "/saveFallRiskScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveFallRiskScore(@RequestBody FallRiskScoreDto dto) {
        try {
            return iNursingScaleScoreService.saveFallRiskScore(dto);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSpecialityMappedNursingScoreTabs/{unitId}/{organizationId}/{doctorId}/{specialityId}", method = RequestMethod.GET)
    public @ResponseBody Response getSpecialityMappedNursingScoreTabs(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("doctorId") Integer doctorId,
                                                          @PathVariable("specialityId") Integer specialityId) {
        try {
            return iNursingScaleScoreService.getSpecialityMappedNursingScoreTabs(unitId, organizationId, doctorId, specialityId);
        } catch (Exception e) {
            throw e;
        }
    }

}
