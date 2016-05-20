package org.fao.etl.layer.fenix.dto;

import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;

import java.util.Collection;
import java.util.LinkedList;

public class MetadataGroups {
    public Collection<MeIdentification> insert = new LinkedList<>();
    public Collection<MeIdentification> update = new LinkedList<>();
    public Collection<MeIdentification> delete = new LinkedList<>();
}
