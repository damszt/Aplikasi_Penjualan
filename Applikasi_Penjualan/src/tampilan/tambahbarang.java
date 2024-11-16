/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tampilan;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import koneksi.koneksi;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author zz
 */
public class tambahbarang extends javax.swing.JFrame {
public String idsup, namasup, tlpsup, emailsup, almtsup;
public String kdbrg, nmbrg, jenisbrg, hb, hj, stok;
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    /**
     * Creates new form nota
     */
    public tambahbarang() {
        initComponents();
        String kd = UserID.getUserLogin();
        idksr.setText(kd);
        nama();
        kosong();
        aktif();
        autonumber();
    }
    
    
    public void cetak(){
        try {
            String path ="./src/report/reportbarangmasuk.jasper";
            HashMap parameter = new HashMap();
            parameter.put("id_notatmb", txtidnota.getText());
            JasperPrint print = JasperFillManager.fillReport(path, parameter, conn);
            //JasperExportManager.exportReportToPdfFile(path, "./src/report/reportnota.jasper.pdf");
            JasperViewer.viewReport(print, false);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane,"Dokumen Tidak Ada"+ ex);
        }
    }
    
    
    protected void nama(){
        try{
            String sql ="SELECT * FROM kasir WHERE id_kasir='"+idksr.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            if (hasil.next()){
                namaksr.setText(hasil.getString("nama_kasir"));
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal dipanggil"+e);
        }
    }
    
    protected void aktif(){
        txtqty.requestFocus();
        jtgl.setEditor(new JSpinner.DateEditor(jtgl,"yyyy/MM/dd HH:mm:ss"));
        Object[]Baris = {"KD Barang","Nama","Harga Beli","Harga Jual","QTY","Total"};
        tabmode = new DefaultTableModel(null, Baris);
        tblnota.setModel(tabmode);
    }
    
    protected void kosong(){
        txtidsup.setText("");
        txtnmsup.setText("");
        txttlpsup.setText("");
        txtmailsup.setText("");
        txtalmtsup.setText("");
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtstok.setText("");
        txtqty.setText("");
        txttotal.setText("");
        txtttotal.setText("");
    }
    
    protected void autonumber(){
    try {
        String sql = "SELECT id_notatmb From notatambah order by id_notatmb asc";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
       txtidnota.setText("AD0001");
       while (rs.next()){
           String id_nota = rs.getString("id_notatmb").substring(2);
           int AN = Integer.parseInt(id_nota)+1;
           String Nol = "";
           if (AN<10)
           {Nol = "000";}
           else if(AN<100)
             {Nol = "00";}
             else if(AN<1000)
             {Nol = "0";} 
             else if(AN<1000)
             {Nol = "";}  
           txtidnota.setText("AD"+Nol+ AN);    
  }
    } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "AUTO NUMBER GAGAL!!"+e);
        }
    }
    
    public void updatestok(){
        DefaultTableModel table2 =(DefaultTableModel) tblnota.getModel();
        for (int i=0; i < table2.getRowCount(); i++){
            String kdbrg = table2.getValueAt(i, 0).toString();
            String qtybarang = table2.getValueAt (i, 4).toString();
            try{
                String sql = "SELECT * FROM barang where kdbrg = '"+kdbrg+"'";
                Statement stat =  conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql);
                if (hasil.next()){
                   String kodebarang = hasil.getString("kdbrg");
                   String stokygingindikurangi = hasil.getString("stok");
                   int sisastock = Integer.valueOf(stokygingindikurangi)+ Integer.valueOf(qtybarang);
                   String query ="update barang set stok = '"+sisastock+"' where kdbrg = '"+kodebarang+"'";
                   //System.out.println(query);
                   stat.executeUpdate(query);
                
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " Stok telah diubah");
    }
        
 }}
    
    public void itemTerpilih(){
        popupsupplier Pps = new popupsupplier();
        Pps.popsup = this;
        txtidsup.setText(idsup);
        txtnmsup.setText(namasup);
        txttlpsup.setText(tlpsup);
        txtmailsup.setText(emailsup);
        txtalmtsup.setText(almtsup);
    }
    
    public void itemTerpilihBrg(){
        popupbarangsup Ptambah = new popupbarangsup();
        Ptambah.tambah= this;
        txtkdbrg.setText(kdbrg);
        txtnmbrg.setText(nmbrg);
        txthb.setText(hb);
        txthj.setText(hj);
        txtstok.setText(stok);
        txtqty.requestFocus();
    }
    
    public void hitung(){
        int total = 0;
        for (int i =0; i<tblnota.getRowCount(); i++){
                int amount = Integer.valueOf(tblnota.getValueAt(i, 5).toString());
                total += amount;
        }
        txtttotal.setText(Integer.toString(total));
    }
    
    
      
            
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        namaksr = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtidnota = new javax.swing.JTextField();
        bcarip = new javax.swing.JButton();
        txtidsup = new javax.swing.JTextField();
        txtnmsup = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtalmtsup = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jtgl = new javax.swing.JSpinner();
        txtnmbrg = new javax.swing.JTextField();
        txthb = new javax.swing.JTextField();
        txtqty = new javax.swing.JTextField();
        txthj = new javax.swing.JTextField();
        txttotal = new javax.swing.JTextField();
        btambah = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblnota = new javax.swing.JTable();
        bsimpan = new javax.swing.JButton();
        bbatal = new javax.swing.JButton();
        bkeluar = new javax.swing.JButton();
        bhapus = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtttotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtkdbrg = new javax.swing.JTextField();
        bcaribrg = new javax.swing.JButton();
        idksr = new javax.swing.JLabel();
        txtmailsup = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        bsendmail = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtstok = new javax.swing.JTextField();
        txttlpsup = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("ID Nota");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 66, -1, -1));

        jLabel3.setText("Data Pelanggan");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 101, -1, -1));

        jLabel4.setText("ID Supplier");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 136, -1, -1));

        jLabel5.setText("Supplier");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 178, -1, -1));

        jLabel6.setText("Alamat");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 277, -1, -1));

        namaksr.setText("Nama Kasir");
        getContentPane().add(namaksr, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 31, -1, -1));

        jLabel8.setText("Tgl Nota");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 66, -1, -1));

        jLabel9.setText("Data Barang");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 116, -1, -1));

        jLabel10.setText("Nama Barang");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        jLabel11.setText("Harga Beli");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 212, -1, -1));

        jLabel12.setText("Harga Jual");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 240, -1, -1));

        txtidnota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidnotaActionPerformed(evt);
            }
        });
        getContentPane().add(txtidnota, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 63, 195, -1));

        bcarip.setText("Cari");
        bcarip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaripActionPerformed(evt);
            }
        });
        getContentPane().add(bcarip, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 133, -1, 20));

        txtidsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidsupActionPerformed(evt);
            }
        });
        getContentPane().add(txtidsup, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 133, 134, -1));

        txtnmsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmsupActionPerformed(evt);
            }
        });
        getContentPane().add(txtnmsup, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 175, 195, -1));

        txtalmtsup.setColumns(20);
        txtalmtsup.setRows(5);
        jScrollPane1.setViewportView(txtalmtsup);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 277, 195, 59));

        jLabel13.setText("QTY");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 304, -1, -1));

        jLabel14.setText("Total");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 337, -1, -1));

        jtgl.setModel(new javax.swing.SpinnerDateModel());
        jtgl.setEditor(new javax.swing.JSpinner.DateEditor(jtgl, "dd-MM-yyyy HH:mm:ss"));
        getContentPane().add(jtgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 63, 242, -1));

        txtnmbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmbrgActionPerformed(evt);
            }
        });
        getContentPane().add(txtnmbrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(602, 170, 240, -1));

        txthb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthbActionPerformed(evt);
            }
        });
        getContentPane().add(txthb, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 210, 240, -1));

        txtqty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqtyActionPerformed(evt);
            }
        });
        getContentPane().add(txtqty, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 301, 242, -1));

        txthj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthjActionPerformed(evt);
            }
        });
        getContentPane().add(txthj, new org.netbeans.lib.awtextra.AbsoluteConstraints(602, 240, 240, -1));

        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });
        getContentPane().add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 334, 242, -1));

        btambah.setText("Tambah");
        btambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambahActionPerformed(evt);
            }
        });
        getContentPane().add(btambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 360, 377, -1));

        tblnota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblnota);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 386, 820, 68));

        bsimpan.setText("Simpan");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });
        getContentPane().add(bsimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 506, 87, -1));

        bbatal.setText("Batal");
        bbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbatalActionPerformed(evt);
            }
        });
        getContentPane().add(bbatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 506, 87, -1));

        bkeluar.setText("Keluar");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });
        getContentPane().add(bkeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 472, 87, -1));

        bhapus.setText("Hapus");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });
        getContentPane().add(bhapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 472, 87, -1));

        jLabel15.setText("Tota Hargal");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 476, -1, -1));
        getContentPane().add(txtttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 473, 223, -1));

        jLabel1.setText("Transaksi");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 354, -1, -1));

        jLabel18.setText("KD Barang");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 148, 64, -1));

        txtkdbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkdbrgActionPerformed(evt);
            }
        });
        txtkdbrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyTyped(evt);
            }
        });
        getContentPane().add(txtkdbrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 142, 181, -1));

        bcaribrg.setText("Cari");
        bcaribrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaribrgActionPerformed(evt);
            }
        });
        getContentPane().add(bcaribrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 142, 55, 20));

        idksr.setText("ID Kasir");
        getContentPane().add(idksr, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 31, -1, -1));

        txtmailsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmailsupActionPerformed(evt);
            }
        });
        getContentPane().add(txtmailsup, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 244, 195, -1));

        jLabel7.setText("Email");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 244, -1, 20));

        bsendmail.setText("Send @");
        bsendmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsendmailActionPerformed(evt);
            }
        });
        getContentPane().add(bsendmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 472, 87, -1));

        jLabel16.setText("Stok");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 273, -1, -1));
        getContentPane().add(txtstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 270, 242, -1));

        txttlpsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttlpsupActionPerformed(evt);
            }
        });
        getContentPane().add(txttlpsup, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 206, 195, -1));

        jLabel17.setText("Telepon");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 212, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txthbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthbActionPerformed

    private void txthjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthjActionPerformed

    private void txtnmbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmbrgActionPerformed

    private void txtkdbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkdbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkdbrgActionPerformed

    private void bcaripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaripActionPerformed
        // TODO add your handling code here:
        popupsupplier Pps = new popupsupplier();
        Pps.popsup = this;
        Pps.setVisible(true);
        Pps.setResizable(false);
    }//GEN-LAST:event_bcaripActionPerformed

    private void bcaribrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaribrgActionPerformed
        // TODO add your handling code here:
        popupbarangsup Pbrg = new popupbarangsup();
        Pbrg.tambah = this;
        Pbrg.setVisible(true);
        Pbrg.setResizable(false);
    }//GEN-LAST:event_bcaribrgActionPerformed

    private void txtqtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqtyActionPerformed
        // TODO add your handling code here:
        int xhrgb = Integer.parseInt(txthb.getText());
        int xqty = Integer.parseInt(txtqty.getText());
        int xjml=xhrgb*xqty;
        txttotal.setText(String.valueOf(xjml));
        
        
    }//GEN-LAST:event_txtqtyActionPerformed

    private void btambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambahActionPerformed
        // TODO add your handling code here:
        try{
            String kode = txtkdbrg.getText();
            String nama = txtnmbrg.getText();
            int hargap = Integer.parseInt(txthb.getText());
            int hargaj = Integer.parseInt(txthj.getText());
            int qty = Integer.parseInt(txtqty.getText());
            int total = Integer.parseInt(txttotal.getText());
        
            
            
            tabmode.addRow(new Object[]{kode,nama,hargap,hargaj,qty,total});
            tblnota.setModel(tabmode);
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e);
        }
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtstok.setText("");
        txtqty.setText("");
        txttotal.setText("");
        hitung();
    }//GEN-LAST:event_btambahActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        // TODO add your handling code here:
        int index = tblnota.getSelectedRow();
        tabmode.removeRow(index);
        tblnota.setModel(tabmode);
        hitung();
    }//GEN-LAST:event_bhapusActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fd = sdf.format(jtgl.getValue());
        String sql = "insert into notatambah values (?,?,?,?,?)";
        String zsql = "insert into isitambah values (?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtidnota.getText());
            stat.setString(2, fd);
            stat.setString(3, txtidsup.getText());
            stat.setString(4, txtmailsup.getText());
            stat.setString(5, idksr.getText());
            
            stat.executeUpdate();
            int t = tblnota.getRowCount();
            for(int i =0; i < t; i++)
            {
                String xkd = tblnota.getValueAt(i, 0).toString();
                String xhb = tblnota.getValueAt(i, 2).toString();
                String xhj = tblnota.getValueAt(i, 3).toString();
                String xqty = tblnota.getValueAt(i, 4).toString();
             
           PreparedStatement stat2 = conn.prepareStatement(zsql);
            stat2.setString(1, txtidnota.getText());
            stat2.setString(2, xkd);
            stat2.setString(3, xhb);
            stat2.setString(4, xhj);
            stat2.setString(5, xqty);
            
            stat2.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, " Berhasil Disimpan");
            cetak();
            updatestok();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal Disimpan" +e);
        }
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bbatalActionPerformed

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_bkeluarActionPerformed

    private void txtidnotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidnotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidnotaActionPerformed

    private void txtnmsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmsupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmsupActionPerformed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void txtidsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidsupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidsupActionPerformed

    private void txtmailsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmailsupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmailsupActionPerformed

    private void bsendmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsendmailActionPerformed
        // TODO add your handling code here:
      //  mail_api mail = new mail_api();
       // mail.sendmail(txtmailp.getText(), txtidnota.getText(), txtttotal.getText());
    }//GEN-LAST:event_bsendmailActionPerformed

    private void txtkdbrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyPressed
        // TODO add your handling code here:
        /* if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
        DefaultTableModel table = new DefaultTableModel();
        
        table.addColumn("NO");
        table.addColumn("CODE ITEM");
        table.addColumn("NAME ITEM");
        table.addColumn("PRICE");
        table.addColumn("STOCK");
        
        try
        {
            String sql ="SELECT * FROM barang where kdbrg='"+txtkdbrg.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            

            
            while(hasil.next())
            {
                table.addRow(new Object[]{
                    
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                        
                });
            }
            tblnota.setModel(table);
        }
        catch(Exception e){
    
}
        }*/
        
    }//GEN-LAST:event_txtkdbrgKeyPressed

    private void txtkdbrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyTyped
        // TODO add your handling code here:
        /* if("".equals(txtkdbrg.getText())){
            listed();
        }*/
    }//GEN-LAST:event_txtkdbrgKeyTyped

    private void txtkdbrgKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyReleased
        // TODO add your handling code here:
       /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
        DefaultTableModel table = new DefaultTableModel();
        
        table.addColumn("NO");
        table.addColumn("CODE ITEM");
        table.addColumn("NAME ITEM");
        table.addColumn("PRICE");
        table.addColumn("STOCK");
        
        try
        {
            String sql ="SELECT * FROM barang where kdbrg='"+txtkdbrg.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while(hasil.next())
            {
                table.addRow(new Object[]{
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                        
                });
            }
            tblnota.setModel(table);
        }
        catch(Exception e){
    
}
        }*/
    }//GEN-LAST:event_txtkdbrgKeyReleased

    private void txttlpsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttlpsupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttlpsupActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tambahbarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tambahbarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tambahbarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tambahbarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tambahbarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbatal;
    private javax.swing.JButton bcaribrg;
    private javax.swing.JButton bcarip;
    private javax.swing.JButton bhapus;
    private javax.swing.JButton bkeluar;
    private javax.swing.JButton bsendmail;
    private javax.swing.JButton bsimpan;
    private javax.swing.JButton btambah;
    private javax.swing.JLabel idksr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jtgl;
    private javax.swing.JLabel namaksr;
    private javax.swing.JTable tblnota;
    private javax.swing.JTextArea txtalmtsup;
    private javax.swing.JTextField txthb;
    private javax.swing.JTextField txthj;
    private javax.swing.JTextField txtidnota;
    private javax.swing.JTextField txtidsup;
    private javax.swing.JTextField txtkdbrg;
    private javax.swing.JTextField txtmailsup;
    private javax.swing.JTextField txtnmbrg;
    private javax.swing.JTextField txtnmsup;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txttlpsup;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txtttotal;
    // End of variables declaration//GEN-END:variables
}
