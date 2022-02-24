package com.example.demo.controllers;

import com.example.demo.Models.Dosis;
import com.example.demo.Models.Solicitante;
import com.example.demo.Models.Vacuna;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/dosis")
public class DosisController {
    private Dosis dosis = new Dosis();

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Solicitante> findAll(){
        List dosises = new ArrayList<Dosis>();
        int cont = 0;
        try {
            String path = "C:\\Users\\David Andre\\Downloads\\demo\\demo\\src\\main\\resources\\Vacunacion.owl" ;
            Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(path);
            ArrayList<Resource> results = new ArrayList<Resource>();
            ExtendedIterator individuals = ((OntModel) model).listIndividuals();
            while (individuals.hasNext()) {
                Resource individual = (Resource) individuals.next();
                results.add(individual);
            }
            System.out.println("\n");
            for(int i = 0; i < results.size(); i++)
            {
                Individual ind = ((OntModel) model).getIndividual(results.get(i).toString());
                if(ind.getOntClass().getLocalName().toString().equals("Dosis")){
                    cont = 0;
                    dosis = new Dosis();
                    dosis.setUri(ind.getURI());
                    System.out.println("{ uri:"+ind.getOntClass());
                    StmtIterator it = ind.listProperties();

                    while ( it.hasNext()) {
                        Statement s = (Statement) it.next();
                        if (s.getObject().isLiteral()) {
                            if(cont == 0){
                                dosis.setVacunador(s.getLiteral().getLexicalForm().toString());
                            }
                            if(cont == 1){
                                dosis.setFecha(s.getLiteral().getLexicalForm().toString());
                            }
                            System.out.println(""+s.getPredicate().getLocalName()+":"+s.getLiteral().getLexicalForm().toString());
                            cont++;

                        }
                        else {
                            System.out.println("" + s.getObject().toString().substring(45) + " type = " + s.getPredicate().getLocalName());
                        };
                    }
                    dosises.add(dosis);
                    System.out.println("}\n");
                }
            }
            return dosises;
        }
        catch (Exception e){
            throw e;
        }
    }
}
