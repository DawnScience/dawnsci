/*-
 *******************************************************************************
 * Copyright (c) 2015 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This file was auto-generated from the NXDL XML definition.
 * Generated at: 2015-10-12T11:55:04.232+01:00
 *******************************************************************************/

package org.eclipse.dawnsci.nexus.impl;

import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;

import org.eclipse.dawnsci.nexus.*;

/**
 * TODO: need documentation
 * 
 * @version 1.0
 */
public class NXdisk_chopperImpl extends NXobjectImpl implements NXdisk_chopper {

	private static final long serialVersionUID = 1L;  // no state in this class, so always compatible

	public static final String NX_TYPE = "type";
	public static final String NX_ROTATION_SPEED = "rotation_speed";
	public static final String NX_SLITS = "slits";
	public static final String NX_SLIT_ANGLE = "slit_angle";
	public static final String NX_PAIR_SEPARATION = "pair_separation";
	public static final String NX_RADIUS = "radius";
	public static final String NX_SLIT_HEIGHT = "slit_height";
	public static final String NX_PHASE = "phase";
	public static final String NX_RATIO = "ratio";
	public static final String NX_DISTANCE = "distance";
	public static final String NX_WAVELENGTH_RANGE = "wavelength_range";

	protected NXdisk_chopperImpl(final NexusNodeFactory nodeFactory) {
		super(nodeFactory);
	}

	protected NXdisk_chopperImpl(final long oid) {
		super(oid);
	}
	
	@Override
	public Class<? extends NXobject> getNXclass() {
		return NXdisk_chopper.class;
	}
	
	@Override
	public NXbaseClass getNXbaseClass() {
		return NXbaseClass.NX_DISK_CHOPPER;
	}

	@Override
	public IDataset getType() {
		return getDataset(NX_TYPE);
	}

	@Override
	public String getScalarType() {
		return getString(NX_TYPE);
	}

	public void setType(IDataset type) {
		setDataset(NX_TYPE, type);
	}

	public void setScalarType(String type) {
		setString(NX_TYPE, type);
	}

	@Override
	public IDataset getRotation_speed() {
		return getDataset(NX_ROTATION_SPEED);
	}

	@Override
	public double getScalarRotation_speed() {
		return getDouble(NX_ROTATION_SPEED);
	}

	public void setRotation_speed(IDataset rotation_speed) {
		setDataset(NX_ROTATION_SPEED, rotation_speed);
	}

	public void setScalarRotation_speed(double rotation_speed) {
		setField(NX_ROTATION_SPEED, rotation_speed);
	}

	@Override
	public IDataset getSlits() {
		return getDataset(NX_SLITS);
	}

	@Override
	public long getScalarSlits() {
		return getLong(NX_SLITS);
	}

	public void setSlits(IDataset slits) {
		setDataset(NX_SLITS, slits);
	}

	public void setScalarSlits(long slits) {
		setField(NX_SLITS, slits);
	}

	@Override
	public IDataset getSlit_angle() {
		return getDataset(NX_SLIT_ANGLE);
	}

	@Override
	public double getScalarSlit_angle() {
		return getDouble(NX_SLIT_ANGLE);
	}

	public void setSlit_angle(IDataset slit_angle) {
		setDataset(NX_SLIT_ANGLE, slit_angle);
	}

	public void setScalarSlit_angle(double slit_angle) {
		setField(NX_SLIT_ANGLE, slit_angle);
	}

	@Override
	public IDataset getPair_separation() {
		return getDataset(NX_PAIR_SEPARATION);
	}

	@Override
	public double getScalarPair_separation() {
		return getDouble(NX_PAIR_SEPARATION);
	}

	public void setPair_separation(IDataset pair_separation) {
		setDataset(NX_PAIR_SEPARATION, pair_separation);
	}

	public void setScalarPair_separation(double pair_separation) {
		setField(NX_PAIR_SEPARATION, pair_separation);
	}

	@Override
	public IDataset getRadius() {
		return getDataset(NX_RADIUS);
	}

	@Override
	public double getScalarRadius() {
		return getDouble(NX_RADIUS);
	}

	public void setRadius(IDataset radius) {
		setDataset(NX_RADIUS, radius);
	}

	public void setScalarRadius(double radius) {
		setField(NX_RADIUS, radius);
	}

	@Override
	public IDataset getSlit_height() {
		return getDataset(NX_SLIT_HEIGHT);
	}

	@Override
	public double getScalarSlit_height() {
		return getDouble(NX_SLIT_HEIGHT);
	}

	public void setSlit_height(IDataset slit_height) {
		setDataset(NX_SLIT_HEIGHT, slit_height);
	}

	public void setScalarSlit_height(double slit_height) {
		setField(NX_SLIT_HEIGHT, slit_height);
	}

	@Override
	public IDataset getPhase() {
		return getDataset(NX_PHASE);
	}

	@Override
	public double getScalarPhase() {
		return getDouble(NX_PHASE);
	}

	public void setPhase(IDataset phase) {
		setDataset(NX_PHASE, phase);
	}

	public void setScalarPhase(double phase) {
		setField(NX_PHASE, phase);
	}

	@Override
	public IDataset getRatio() {
		return getDataset(NX_RATIO);
	}

	@Override
	public long getScalarRatio() {
		return getLong(NX_RATIO);
	}

	public void setRatio(IDataset ratio) {
		setDataset(NX_RATIO, ratio);
	}

	public void setScalarRatio(long ratio) {
		setField(NX_RATIO, ratio);
	}

	@Override
	public IDataset getDistance() {
		return getDataset(NX_DISTANCE);
	}

	@Override
	public double getScalarDistance() {
		return getDouble(NX_DISTANCE);
	}

	public void setDistance(IDataset distance) {
		setDataset(NX_DISTANCE, distance);
	}

	public void setScalarDistance(double distance) {
		setField(NX_DISTANCE, distance);
	}

	@Override
	public IDataset getWavelength_range() {
		return getDataset(NX_WAVELENGTH_RANGE);
	}

	@Override
	public double getScalarWavelength_range() {
		return getDouble(NX_WAVELENGTH_RANGE);
	}

	public void setWavelength_range(IDataset wavelength_range) {
		setDataset(NX_WAVELENGTH_RANGE, wavelength_range);
	}

	public void setScalarWavelength_range(double wavelength_range) {
		setField(NX_WAVELENGTH_RANGE, wavelength_range);
	}

	@Override
	public NXgeometry getGeometry() {
		return getChild("geometry", NXgeometry.class);
	}

	public void setGeometry(NXgeometry geometry) {
		putChild("geometry", geometry);
	}

	@Override
	public NXgeometry getGeometry(String name) {
		return getChild(name, NXgeometry.class);
	}

	public void setGeometry(String name, NXgeometry geometry) {
		putChild(name, geometry);
	}

	@Override
	public Map<String, NXgeometry> getAllGeometry() {
		return getChildren(NXgeometry.class);
	}

	public void setAllGeometry(Map<String, NXgeometry> geometry) {
		setChildren(geometry);
	}

}
