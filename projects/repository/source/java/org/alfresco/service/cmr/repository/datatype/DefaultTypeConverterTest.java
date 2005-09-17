/*
 * Copyright (C) 2005 Alfresco, Inc.
 *
 * Licensed under the Mozilla Public License version 1.1 
 * with a permitted attribution clause. You may obtain a
 * copy of the License at
 *
 *   http://www.alfresco.org/legal/license.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package org.alfresco.service.cmr.repository.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.alfresco.util.ISO8601DateFormat;

public class DefaultTypeConverterTest extends TestCase
{

    public DefaultTypeConverterTest()
    {
        super();
    }

    public DefaultTypeConverterTest(String arg0)
    {
        super(arg0);
    }

    public void testPrimitives()
    {
        assertEquals(Boolean.valueOf(false), DefaultTypeConverter.INSTANCE.convert(Boolean.class, false));
        assertEquals(Boolean.valueOf(true), DefaultTypeConverter.INSTANCE.convert(Boolean.class, true));
        assertEquals(Character.valueOf('a'), DefaultTypeConverter.INSTANCE.convert(Character.class, 'a'));
        assertEquals(Byte.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Byte.class, (byte) 3));
        assertEquals(Short.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Short.class, (short) 4));
        assertEquals(Integer.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Integer.class, (int) 5));
        assertEquals(Long.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Long.class, (long) 6));
        assertEquals(Float.valueOf("7.1"), DefaultTypeConverter.INSTANCE.convert(Float.class, (float) 7.1));
        assertEquals(Double.valueOf("123.123"), DefaultTypeConverter.INSTANCE.convert(Double.class, (double) 123.123));
    }

    public void testNoConversion()
    {
        assertEquals(Boolean.valueOf(false), DefaultTypeConverter.INSTANCE.convert(Boolean.class, Boolean.valueOf(false)));
        assertEquals(Boolean.valueOf(true), DefaultTypeConverter.INSTANCE.convert(Boolean.class, Boolean.valueOf(true)));
        assertEquals(Character.valueOf('w'), DefaultTypeConverter.INSTANCE.convert(Character.class, Character.valueOf('w')));
        assertEquals(Byte.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Byte.valueOf("3")));
        assertEquals(Short.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Short.class, Short.valueOf("4")));
        assertEquals(Integer.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Integer.valueOf("5")));
        assertEquals(Long.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Long.class, Long.valueOf("6")));
        assertEquals(Float.valueOf("7.1"), DefaultTypeConverter.INSTANCE.convert(Float.class, Float.valueOf("7.1")));
        assertEquals(Double.valueOf("123.123"), DefaultTypeConverter.INSTANCE.convert(Double.class, Double.valueOf("123.123")));
        assertEquals(Double.valueOf("123.123"), DefaultTypeConverter.INSTANCE.convert(Double.class, Double.valueOf("123.123")));
        assertEquals(new BigInteger("1234567890123456789"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, new BigInteger("1234567890123456789")));
        assertEquals(new BigDecimal("12345678901234567890.12345678901234567890"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, new BigDecimal("12345678901234567890.12345678901234567890")));
        Date date = new Date();
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, date));
        assertEquals(new Duration("P25D"), DefaultTypeConverter.INSTANCE.convert(Duration.class, new Duration("P25D")));
        assertEquals("woof", DefaultTypeConverter.INSTANCE.convert(String.class, "woof"));
    }

    public void testToString()
    {
        assertEquals("true", DefaultTypeConverter.INSTANCE.convert(String.class, new Boolean(true)));
        assertEquals("false", DefaultTypeConverter.INSTANCE.convert(String.class, new Boolean(false)));
        assertEquals("v", DefaultTypeConverter.INSTANCE.convert(String.class, Character.valueOf('v')));
        assertEquals("3", DefaultTypeConverter.INSTANCE.convert(String.class, Byte.valueOf("3")));
        assertEquals("4", DefaultTypeConverter.INSTANCE.convert(String.class, Short.valueOf("4")));
        assertEquals("5", DefaultTypeConverter.INSTANCE.convert(String.class, Integer.valueOf("5")));
        assertEquals("6", DefaultTypeConverter.INSTANCE.convert(String.class, Long.valueOf("6")));
        assertEquals("7.1", DefaultTypeConverter.INSTANCE.convert(String.class, Float.valueOf("7.1")));
        assertEquals("123.123", DefaultTypeConverter.INSTANCE.convert(String.class, Double.valueOf("123.123")));
        assertEquals("1234567890123456789", DefaultTypeConverter.INSTANCE.convert(String.class, new BigInteger("1234567890123456789")));
        assertEquals("12345678901234567890.12345678901234567890", DefaultTypeConverter.INSTANCE.convert(String.class, new BigDecimal("12345678901234567890.12345678901234567890")));
        Date date = new Date();
        assertEquals(ISO8601DateFormat.format(date), DefaultTypeConverter.INSTANCE.convert(String.class, date));
        assertEquals("P0Y25D", DefaultTypeConverter.INSTANCE.convert(String.class, new Duration("P0Y25D")));
        assertEquals("woof", DefaultTypeConverter.INSTANCE.convert(String.class, "woof"));
    }

    public void testFromString()
    {
        assertEquals(Boolean.valueOf(true), DefaultTypeConverter.INSTANCE.convert(Boolean.class, "True"));
        assertEquals(Boolean.valueOf(false), DefaultTypeConverter.INSTANCE.convert(Boolean.class, "woof"));
        assertEquals(Character.valueOf('w'), DefaultTypeConverter.INSTANCE.convert(Character.class, "w"));
        assertEquals(Byte.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Byte.class, "3"));
        assertEquals(Short.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Short.class, "4"));
        assertEquals(Integer.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Integer.class, "5"));
        assertEquals(Long.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Long.class, "6"));
        assertEquals(Float.valueOf("7.1"), DefaultTypeConverter.INSTANCE.convert(Float.class, "7.1"));
        assertEquals(Double.valueOf("123.123"), DefaultTypeConverter.INSTANCE.convert(Double.class, "123.123"));
        assertEquals(new BigInteger("1234567890123456789"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, "1234567890123456789"));
        assertEquals(new BigDecimal("12345678901234567890.12345678901234567890"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, "12345678901234567890.12345678901234567890"));
        assertEquals("2004-03-12T00:00:00.000Z", ISO8601DateFormat.format(DefaultTypeConverter.INSTANCE.convert(Date.class, "2004-03-12T00:00:00.000Z")));
        assertEquals(new Duration("P25D"), DefaultTypeConverter.INSTANCE.convert(Duration.class, "P25D"));
        assertEquals("woof", DefaultTypeConverter.INSTANCE.convert(String.class, "woof"));
    }

    public void testPrimativeAccessors()
    {
        assertEquals(false, DefaultTypeConverter.INSTANCE.convert(Boolean.class, false).booleanValue());
        assertEquals(true, DefaultTypeConverter.INSTANCE.convert(Boolean.class, true).booleanValue());
        assertEquals('a', DefaultTypeConverter.INSTANCE.convert(Character.class, 'a').charValue());
        assertEquals((byte) 3, DefaultTypeConverter.INSTANCE.convert(Byte.class, (byte) 3).byteValue());
        assertEquals((short) 4, DefaultTypeConverter.INSTANCE.convert(Short.class, (short) 4).shortValue());
        assertEquals((int) 5, DefaultTypeConverter.INSTANCE.convert(Integer.class, (int) 5).intValue());
        assertEquals((long) 6, DefaultTypeConverter.INSTANCE.convert(Long.class, (long) 6).longValue());
        assertEquals((float) 7.1, DefaultTypeConverter.INSTANCE.convert(Float.class, (float) 7.1).floatValue());
        assertEquals((double) 123.123, DefaultTypeConverter.INSTANCE.convert(Double.class, (double) 123.123).doubleValue());
    }
    
    public void testInterConversions()
    {
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Byte.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Byte.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Byte.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Byte.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Byte.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Byte.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Byte.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Byte.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Short.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Short.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Short.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Short.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Short.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Short.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Short.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Short.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Integer.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Integer.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Integer.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Integer.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Integer.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Integer.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Integer.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Integer.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Long.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Long.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Long.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Long.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Long.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Long.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Long.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Long.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Float.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Float.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Float.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Float.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Float.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Float.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Float.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Float.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, Double.valueOf("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, Double.valueOf("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, Double.valueOf("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, Double.valueOf("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, Double.valueOf("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, Double.valueOf("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, Double.valueOf("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, Double.valueOf("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, new BigInteger("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, new BigInteger("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, new BigInteger("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, new BigInteger("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, new BigInteger("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, new BigInteger("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, new BigInteger("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, new BigInteger("8")));
        
        assertEquals(Byte.valueOf("1"), DefaultTypeConverter.INSTANCE.convert(Byte.class, new BigDecimal("1")));
        assertEquals(Short.valueOf("2"), DefaultTypeConverter.INSTANCE.convert(Short.class, new BigDecimal("2")));
        assertEquals(Integer.valueOf("3"), DefaultTypeConverter.INSTANCE.convert(Integer.class, new BigDecimal("3")));
        assertEquals(Long.valueOf("4"), DefaultTypeConverter.INSTANCE.convert(Long.class, new BigDecimal("4")));
        assertEquals(Float.valueOf("5"), DefaultTypeConverter.INSTANCE.convert(Float.class, new BigDecimal("5")));
        assertEquals(Double.valueOf("6"), DefaultTypeConverter.INSTANCE.convert(Double.class, new BigDecimal("6")));
        assertEquals(new BigInteger("7"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, new BigDecimal("7")));
        assertEquals(new BigDecimal("8"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, new BigDecimal("8")));
    }
    
    public void testDate()
    {
        Date date = new Date(101);
        
        assertEquals(Byte.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Byte.class, date));
        assertEquals(Short.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Short.class, date));
        assertEquals(Integer.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Integer.class, date));
        assertEquals(Long.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Long.class, date));
        assertEquals(Float.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Float.class, date));
        assertEquals(Double.valueOf("101"), DefaultTypeConverter.INSTANCE.convert(Double.class, date));
        assertEquals(new BigInteger("101"), DefaultTypeConverter.INSTANCE.convert(BigInteger.class, date));
        assertEquals(new BigDecimal("101"), DefaultTypeConverter.INSTANCE.convert(BigDecimal.class, date));
        
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (byte)101));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (short)101));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (int)101));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (long)101));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (float)101));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (double)101));
        
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, new BigInteger("101")));
        assertEquals(date, DefaultTypeConverter.INSTANCE.convert(Date.class, (Object)(new BigDecimal("101"))));
        
        assertEquals(101, DefaultTypeConverter.INSTANCE.intValue(date));
    }
    
    public void testMultiValue()
    {
        ArrayList<Object> list = makeList();
        
        assertEquals(true, DefaultTypeConverter.INSTANCE.isMultiValued(list));
        assertEquals(14, DefaultTypeConverter.INSTANCE.size(list));
        
        for(String stringValue: DefaultTypeConverter.INSTANCE.getCollection(String.class, list))
        {
            System.out.println("Value is "+stringValue); 
        }
        
    }

    private ArrayList<Object> makeList()
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(Boolean.valueOf(true));
        list.add(Boolean.valueOf(false));
        list.add(Character.valueOf('q'));
        list.add(Byte.valueOf("1"));
        list.add(Short.valueOf("2"));
        list.add(Integer.valueOf("3"));
        list.add(Long.valueOf("4"));
        list.add(Float.valueOf("5"));
        list.add(Double.valueOf("6"));
        list.add(new BigInteger("7"));
        list.add(new BigDecimal("8"));
        list.add(new Date());
        list.add(new Duration("P5Y0M"));
        list.add("Hello mum");
        return list;
    }
    
    public void testSingleValuseAsMultiValue()
    {
        Integer integer = Integer.valueOf(43);
        
        assertEquals(false, DefaultTypeConverter.INSTANCE.isMultiValued(integer));
        assertEquals(1, DefaultTypeConverter.INSTANCE.size(integer));
        
        for(String stringValue: DefaultTypeConverter.INSTANCE.getCollection(String.class, integer))
        {
            System.out.println("Value is "+stringValue); 
        }
        
    }
    
    public void testNullAndEmpty()
    {
        assertNull(DefaultTypeConverter.INSTANCE.convert(Boolean.class, null));
        ArrayList<Object> list = new ArrayList<Object>();
        assertNotNull(DefaultTypeConverter.INSTANCE.convert(Boolean.class, list));
        list.add(null);
        assertNotNull(DefaultTypeConverter.INSTANCE.convert(Boolean.class, list));
        
    }
}
