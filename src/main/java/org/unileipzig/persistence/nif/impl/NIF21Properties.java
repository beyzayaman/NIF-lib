package org.unileipzig.persistence.nif.impl;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.unileipzig.persistence.nif.NIF21Format;
import org.unileipzig.persistence.nif.NIFProperties;
import org.unileipzig.persistence.nif.NIFVisitor;

public class NIF21Properties implements NIFProperties, NIF21Format {

   
    public void add(Model model,NIFBean entity) {

        if (model != null && entity != null ) {
            Resource contextRes = model.getResource(entity.getContext().context(CONTEXT_FORMAT));

           if (entity.isMention()) {
               fillMention(model, entity, contextRes);
            } else if (entity.isContext()) {
               fillResourceCollection (model, entity.getContext());
               fillContext(model, contextRes);
            }
        }
    }


    private void fillResourceCollection(Model model, NIFContext context) {

        Resource resource = model.getResource(context.getCollection());

        resource.addProperty(RDF.type, model.createResource(NIF_PROPERTY_CONTEXT_COLLECTION));

        resource.addProperty(
                model.createProperty(NIF_PROPERTY_HAS_CONTEXT),
                model.createResource(context.context(CONTEXT_FORMAT)));


        resource.addProperty(
                model.createProperty(NIF_PROPERTY_CONFORMS_TO),
                model.createResource(context.context(NIF_21)));
    }

    private void fillContext(Model model, Resource contextRes) {


        contextRes.addProperty(
                RDF.type,
                model.createResource(NIF_PROPERTY_CONTEXT));

        contextRes.addProperty(
                RDF.type,
                model.createResource(NIF_PROPERTY_OFFSETBASEDSTRING));

    }

    private void fillMention(Model model, NIFBean entity, Resource contextRes) {

        contextRes.addProperty(
                RDF.type,
                model.createResource(NIF_PROPERTY_OFFSETBASEDSTRING));

        contextRes.addProperty(
                RDF.type,
                model.createResource(NIF_PROPERTY_PHRASE));

        contextRes.addProperty(
                model.createProperty(NIF_PROPERTY_REFERENCE_CONTEXT),
                model.createResource(entity.getReferenceContext()));

    }

   
    public void accept(NIFVisitor visitor) {
        visitor.visit(this);
    }
}
