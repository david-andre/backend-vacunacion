package com.example.demo.controllers;

import com.example.demo.Models.Lugar;
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
@RequestMapping("api/vacuna")
public class VacunaController {
    private Vacuna vacuna = new Vacuna();

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Vacuna> findAll(){
        List vacunas = new ArrayList<Vacuna>();
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
                if(ind.getOntClass().getLocalName().toString().equals("Vacuna")){
                    cont = 0;
                    vacuna = new Vacuna();
                    vacuna.setUri(ind.getURI());
                    System.out.println("{ uri:"+ind.getOntClass());
                    StmtIterator it = ind.listProperties();

                    while ( it.hasNext()) {
                        Statement s = (Statement) it.next();
                        if (s.getObject().isLiteral()) {
                            if(cont == 0){
                                vacuna.setNombre(s.getLiteral().getLexicalForm().toString());
                            }
                            System.out.println(""+s.getPredicate().getLocalName()+":"+s.getLiteral().getLexicalForm().toString());
                            cont++;

                        }
                        else {
                            System.out.println("" + s.getObject().toString().substring(45) + " type = " + s.getPredicate().getLocalName());
                        };
                    }
                    vacunas.add(vacuna);
                    System.out.println("}\n");
                }
            }
            return vacunas;
        }
        catch (Exception e){
            throw e;
        }
    }
}
